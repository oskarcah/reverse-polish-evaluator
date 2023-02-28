package base.evaluador;

import static base.evaluador.Token.Tipo.COMA;
import static base.evaluador.Token.Tipo.FUN;
import static base.evaluador.Token.Tipo.INVALIDO;
import static base.evaluador.Token.Tipo.NIL;
import static base.evaluador.Token.Tipo.NUM;
import static base.evaluador.Token.Tipo.OP;
import static base.evaluador.Token.Tipo.PAR_D;
import static base.evaluador.Token.Tipo.PAR_I;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Clase con métodos de conversión de expresiones de infija a postfija.
 */
public class ConvertidorPostfija {
  private final InfoOperadores infoOperadores;
  private final ExtractorToken extractorToken;
  
  public ConvertidorPostfija(InfoOperadores infoOperadores, ExtractorToken extractorToken) {
    this.infoOperadores = infoOperadores;
    this.extractorToken = extractorToken;
  }
  
  private static void validarNumFun(InfoProximoToken infoProximoToken, Token tokenAnterior) {
    // el primer token no se valida
    if (tokenAnterior == null) {
      return;
    }
    if (NUM.equals(tokenAnterior.getTipo()) || PAR_D.equals(tokenAnterior.getTipo())) {
      throw new ExConversionPostFijo(infoProximoToken,
          "Token no esperado. se espera operador");
    }
    if (FUN.equals(tokenAnterior.getTipo())) {
      throw new ExConversionPostFijo(infoProximoToken,
          "Token no esperado. se espera paréntesis");
    }
  }
  
  private static void validarParDer(InfoProximoToken infoProximoToken, Token tokenAnterior) {
    // el primer token no se valida
    if (tokenAnterior == null) {
      return;
    }
    if (PAR_I.equals(tokenAnterior.getTipo())) {
      throw new ExConversionPostFijo(infoProximoToken,
          "Token no esperado. se espera operador");
    }
    if (OP.equals(tokenAnterior.getTipo()) || FUN.equals(tokenAnterior.getTipo()) ||
        COMA.equals(tokenAnterior.getTipo())) {
      throw new ExConversionPostFijo(infoProximoToken,
          "Token no esperado. se espera número");
    }
  }
  
  private static void validarParIzq(InfoProximoToken infoProximoToken, Token tokenAnterior) {
    // el primer token no se valida
    if (tokenAnterior == null) {
      return;
    }
    if (PAR_D.equals(tokenAnterior.getTipo())) {
      throw new ExConversionPostFijo(infoProximoToken,
          "Token no esperado. no se aceptan paréntesis vacíos");
    }
    if (COMA.equals(tokenAnterior.getTipo())) {
      throw new ExConversionPostFijo(infoProximoToken,
          "Token no esperado. se espera número o funcion");
    }
    if (NUM.equals(tokenAnterior.getTipo())) {
      throw new ExConversionPostFijo(infoProximoToken,
          "Token no esperado. se espera operador o funcion");
    }
  }
  
  private static void validarOp(InfoProximoToken infoProximoToken, Token tokenAnterior) {
    // el primer token no se valida
    if (tokenAnterior == null) {
      return;
    }
    if (COMA.equals(tokenAnterior.getTipo())) {
      throw new ExConversionPostFijo(infoProximoToken,
          "Token no esperado. se espera número o funcion");
    }
    
    if (FUN.equals(tokenAnterior.getTipo())) {
      throw new ExConversionPostFijo(infoProximoToken,
          "Token no esperado. función sin parámetros");
    }
  }
  
  private static void validarComa(InfoProximoToken infoProximoToken, Token tokenAnterior) {
    // el primer token no se valida
    if (tokenAnterior == null) {
      return;
    }
    if (COMA.equals(tokenAnterior.getTipo()) || PAR_I.equals(tokenAnterior.getTipo())) {
      throw new ExConversionPostFijo(infoProximoToken,
          "Token no esperado. se espera número o funcion");
    }
    
    if (FUN.equals(tokenAnterior.getTipo())) {
      throw new ExConversionPostFijo(infoProximoToken,
          "Token no esperado. función sin parámetros");
    }
  }
  
  public List<Token> convertirExpresion(final String infija) {
    // para string vacío se devuelve null
    if (infija == null || infija.trim().isEmpty()) {
      throw new RuntimeException("La expresión es vacia o nula");
    }
    
    final Stack<Token> pila = new Stack<>();
    final List<Token> resultado = new LinkedList<>();
    final InfoProximoToken infoProximoToken = new InfoProximoToken();
    Token tokenAnterior = null;
    while (true) {
      // próximo token
      final Token token = extractorToken.proximoToken(infija, infoProximoToken);
      //Se valida si se llegó al final o se procesa el token actual
      if (NIL.equals(token.getTipo())) {
        break;
      }
      // se valida si el token es inválido
      if (INVALIDO.equals(token.getTipo())) {
        throw new ExTokenInvalido(infoProximoToken, token.getValor());
      }
      if (NUM.equals(token.getTipo())) {
        validarNumFun(infoProximoToken, tokenAnterior);
        // número se lleva directamente al resultado
        resultado.add(token);
        infoProximoToken.menosUnario = false;
      } else if ((FUN.equals(token.getTipo()))) {
        validarNumFun(infoProximoToken, tokenAnterior);
        // nombre de función paréntesis directamente al resultado
        pila.push(token);
        infoProximoToken.menosUnario = true;
      } else if ((PAR_I.equals(token.getTipo()))) {
        validarParIzq(infoProximoToken, tokenAnterior);
        // apertura paréntesis directamente al resultado
        pila.push(token);
        infoProximoToken.menosUnario = true;
      } else if (PAR_D.equals(token.getTipo())) {
        validarParDer(infoProximoToken, tokenAnterior);
        // paréntesis derecho: desapilar hasta encontrar el paréntesis izquierdo
        if (pila.empty()) {
          throw new ExConversionPostFijo(infoProximoToken, "Paréntesis no equilibrados");
        }
        Token tope = pila.pop();
        while (!PAR_I.equals(tope.getTipo())) {
          // los tokens coma se ignoran
          if (!COMA.equals(tope.getTipo())) {
            resultado.add(tope);
          }
          if (pila.empty()) {
            throw new ExConversionPostFijo(infoProximoToken, "Paréntesis no equilibrados");
          }
          tope = pila.pop();
        }
        infoProximoToken.menosUnario = false;
      } else if (OP.equals(token.getTipo())) {
        validarOp(infoProximoToken, tokenAnterior);
        // si es operador se valida la prioridad para desapilar los
        // de menor prioridad en el caso de que la pila no esté vacía
        // lo elementos desapilados se llevan al resultado
        if (!pila.empty()) {
          Token tope = pila.peek();
          while (!pila.empty() && !PAR_I.equals(tope.getTipo()) &&
              (!OP.equals(tope.getTipo()) ||
                  (infoOperadores.compararPrecedenciaOp(tope, token) > 0) ||
                  (infoOperadores.compararPrecedenciaOp(tope, token) == 0 &&
                      infoOperadores.asociatividadOp(token) == 1))) {
            //La coma se ignora
            if (!COMA.equals(tope.getTipo())) {
              resultado.add(tope);
            }
            pila.pop();
            if (!pila.empty()) {
              tope = pila.peek();
            }
          }
        }
        pila.push(token);
        infoProximoToken.menosUnario = true;
      } else if (COMA.equals(token.getTipo())) {
        validarComa(infoProximoToken, tokenAnterior);
        // si es coma, se desapila y se agrega al resultado
        // lo que esté en pila antes de coma o paréntesis izquierdo
        // para que sea evaluado como parametro
        if (pila.empty()) {
          throw new ExConversionPostFijo(infoProximoToken,
              "Coma fuera lista de parametros o como primer parámetro");
        }
        Token tope = pila.peek();
        while (!(PAR_I.equals(tope.getTipo()) || COMA.equals(tope.getTipo()))) {
          resultado.add(tope);
          pila.pop();
          if (!pila.empty()) {
            tope = pila.peek();
          }
        }
        pila.push(token);
        infoProximoToken.menosUnario = true;
      }
      tokenAnterior = token;
    }
    // se termina de sacar el resto de elementos de la pila
    // al resultado
    while (!pila.empty()) {
      Token tope = pila.pop();
      if (!COMA.equals(tope.getTipo())) {
        resultado.add(tope);
      }
      if (PAR_I.equals(tope.getTipo()) || PAR_D.equals(tope.getTipo())) {
        throw new ExConversionPostFijo(infoProximoToken, "Paréntesis no equilibrados");
      }
    }
    return resultado;
  }
}
