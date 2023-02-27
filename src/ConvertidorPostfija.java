import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class ConvertidorPostfija {
  
  private final InfoOperadores infoOperadores;
  private final ExtractorToken extractorToken;
  
  public ConvertidorPostfija(InfoOperadores infoOperadores, ExtractorToken extractorToken) {
    this.infoOperadores = infoOperadores;
    this.extractorToken = extractorToken;
  }
  
  public List<Token> convertirExpresion(final String infija) {
    // para string vacío se devuelve null
    if (infija == null || infija.trim().isEmpty()) {
      return null;
    }
    
    String expresion = infija.replaceAll("\\s+", "");
    int indice = 0;
    boolean menosUnario = false;
    Stack<Token> pilaTokens = new Stack<>();
    List<Token> resultado = new LinkedList<>();
    
    // procesamiento de todos los tokens
    while (true) {
      final Token tokenActual = extractorToken.proximoToken(expresion, indice, menosUnario);
      final Token.Tipo tipoActual = tokenActual.getTipo();
      indice += tokenActual.getLongitud();
      
      /*
       * Se valida si se llegó al final o se procesa el token actual
       */
      if (Token.Tipo.INVALIDO == tipoActual || Token.Tipo.NIL == tipoActual) {
        break;
      }
      
      if (Token.Tipo.NUM == tipoActual) {
        resultado.add(tokenActual);
      } else if (Token.Tipo.FUN == tipoActual || Token.Tipo.PAR_I == tipoActual) {
        pilaTokens.push(tokenActual);
      } else if (Token.Tipo.PAR_D == tipoActual) {
        // desapilar hasta encontrar el paréntesis izquierdo
        Token tokenTope = pilaTokens.pop();
        Token.Tipo tipoTope = tokenTope.getTipo();
        while (Token.Tipo.PAR_I != tipoTope) {
          // los tokens que son coma se ignoran
          if (Token.Tipo.COMA != tipoTope) {
            resultado.add(tokenTope);
          }
          tokenTope = pilaTokens.pop();
          tipoTope = tokenTope.getTipo();
        }
      } else if (Token.Tipo.OP.equals(tipoActual)) {
        // si es operador se valida la prioridad para desapilar los
        // de menor prioridad en el caso de que la pila no esté vacía
        if (!pilaTokens.empty()) {
          Token tokenTope = pilaTokens.peek();
          Token.Tipo tipoTope = tokenTope.getTipo();
          while (!pilaTokens.empty() && (Token.Tipo.OP != tipoTope ||
              (infoOperadores.compararPrecedenciaOp(tokenTope, tokenActual) > 0) ||
              (infoOperadores.compararPrecedenciaOp(tokenTope, tokenActual) == 0 &&
                  infoOperadores.asociatividadOp(tokenActual) == 1)) &&
              Token.Tipo.PAR_I != tipoTope) {
            //La coma se ignora
            if (!Token.Tipo.COMA.equals(tipoTope)) {
              resultado.add(tokenTope);
            }
            pilaTokens.pop();
            if (!pilaTokens.empty()) {
              tokenTope = pilaTokens.peek();
              tipoTope = tokenTope.getTipo();
            }
          }
        }
        pilaTokens.push(tokenActual);
      } else if (Token.Tipo.COMA.equals(tipoActual)) {
        Token tokenTope = pilaTokens.peek();
        Token.Tipo tipoTope = tokenTope.getTipo();
        while (!(Token.Tipo.PAR_I == tipoTope || Token.Tipo.COMA == tipoTope)) {
          resultado.add(tokenTope);
          pilaTokens.pop();
          if (!pilaTokens.empty()) {
            tokenTope = pilaTokens.peek();
            tipoTope = tokenTope.getTipo();
          }
        }
        pilaTokens.push(tokenActual);
      }
    }
    // se termina de sacar el resto de elementos de la pila
    // al resultado
    while (!pilaTokens.empty()) {
      Token tokenTope = pilaTokens.pop();
      if (Token.Tipo.COMA != tokenTope.getTipo()) {
        resultado.add(tokenTope);
      }
    }
    return resultado;
  }
}
