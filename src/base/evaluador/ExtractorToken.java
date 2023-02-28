package base.evaluador;

/**
 * Clase con la lógica para extraer los tokens sintácticos de la expresión infija.
 */
public class ExtractorToken {
  private final InfoOperadores infoOperadores;
  
  public ExtractorToken(final InfoOperadores infoOperadores) {
    this.infoOperadores = infoOperadores;
  }
  
  /**
   * Se busca el próximo string correspondiente a solo letras minúsculas a-z
   * que se corresponda al valor del token FUNC encontrado.
   *
   * @param expresion Expresión a donde se busca el token.
   * @param indice Índice base para la búsqueda del string.
   * @return String literal con el valor del token FUN.
   */
  private String proximoString(final String expresion, final int indice) {
    final int largo = expresion.length();
    if (indice >= largo) {
      return "";
    }
    char c = expresion.charAt(indice);
    int i = indice;
    final StringBuilder resultado = new StringBuilder();
    while (i < largo && infoOperadores.esLetra(c)) {
      resultado.append(c);
      if (++i < largo) {
        c = expresion.charAt(i);
      }
    }
    return resultado.toString();
  }
  
  /**
   * Se busca el próximo string correspondiente a solo letras dígitos
   * que se corresponda al valor del token NUM encontrado.
   *
   * @param expresion Expresión a donde se busca el token.
   * @param indice Índice base para la búsqueda del string.
   * @return String literal con el valor del token NUM.
   */
  private String proximoNumero(final String expresion, final int indice) {
    final int largo = expresion.length();
    if (indice >= largo) {
      return "";
    }
    boolean tienePunto = false;
    final StringBuilder resultado = new StringBuilder();
    int i = indice;
    char c = expresion.charAt(i);
    while (i < largo &&
        (infoOperadores.esDigito(c) || (!tienePunto && infoOperadores.esPunto(c)))) {
      resultado.append(c);
      if (infoOperadores.esPunto(c)) {
        tienePunto = true;
      }
      if (++i < expresion.length()) {
        c = expresion.charAt(i);
      }
    }
    return resultado.toString();
  }
  
  /**
   * Actualiza el objeto base.evaluador.InfoProximoToken con el valor nuevo de posición para la próxima búsqueda
   * del token.
   *
   * @param info Instancia de base.evaluador.InfoProximoToken a modificar.
   * @param longitud longitud del último token obtenido.
   */
  private void actualizarInfo(final InfoProximoToken info, int longitud) {
    info.indice += longitud;
    info.caracter += longitud;
  }
  
  /**
   * Devuelve el próximo token desde un string que representa una expresión, con índice inicial
   * definida en el parámetro info.
   *
   * @param expresion Expresión para calcular el próximo token.
   * @param info      Instancia con la información de posición de caracter y línea.
   * @return Instancia de base.evaluador.Token que contiene el tipo de token y el valor de string obtenido.
   */
  public Token proximoToken(final String expresion, final InfoProximoToken info) {
    final int largo = expresion.length();
    final Token resultado;
    // se ignoran espacios
    while (info.indice < largo && infoOperadores.esEspacio(expresion.charAt(info.indice))) {
      info.caracter++;
      if (infoOperadores.esSaltoLinea(expresion.charAt(info.indice))) {
        info.linea++;
      }
      info.indice++;
    }
    // fin del string
    if (info.indice >= largo) {
      return new Token(Token.Tipo.NIL, "", info.indice, 0);
    }
    final char c = expresion.charAt(info.indice);
    if (infoOperadores.esOperador(c)) {
      final String valor;
      if (c == '-') {
        valor = info.menosUnario
                ? "#"
                : "-";
      } else {
        valor = "" + c;
      }
      resultado = new Token(Token.Tipo.OP, valor, info.indice, 1);
      actualizarInfo(info, 1);
    } else if (c == ')') {
      resultado = new Token(Token.Tipo.PAR_D, ")", info.indice, 1);
      actualizarInfo(info, 1);
    } else if (c == '(') {
      resultado = new Token(Token.Tipo.PAR_I, "(", info.indice, 1);
      actualizarInfo(info, 1);
    } else if (c == ',') {
      resultado = new Token(Token.Tipo.COMA, ",", info.indice, 1);
      actualizarInfo(info, 1);
    } else if (infoOperadores.esLetra(c)) {
      final String valor = proximoString(expresion, info.indice);
      Token.Tipo tipo = infoOperadores.tokenFuncion(valor);
      resultado = new Token(tipo, valor, info.indice, valor.length());
      actualizarInfo(info, valor.length());
    } else if (infoOperadores.esDigito(c) || infoOperadores.esPunto(c)) {
      String valor = proximoNumero(expresion, info.indice);
      resultado = new Token(Token.Tipo.NUM, valor, info.indice, valor.length());
      actualizarInfo(info, valor.length());
    } else {
      resultado = new Token(Token.Tipo.INVALIDO, "" + c, info.indice, 1);
    }
    return resultado;
  }
}
