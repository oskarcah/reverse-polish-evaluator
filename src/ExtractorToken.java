public class ExtractorToken {
  private final InfoOperadores infoOperadores;
  
  public ExtractorToken(final InfoOperadores infoOperadores) {
    this.infoOperadores = infoOperadores;
  }
  
  private boolean esLetra(final char c) {
    return c >= 'a' && c <= 'z';
  }
  
  private boolean esDigito(final char c) {
    return c >= '0' && c <= '9';
  }
  
  private boolean esPunto(final char c) {
    return c == '.';
  }
  
  private boolean esOperador(final char c) {
    return c == '^' || c == '/' || c == '*' || c == '-' || c == '+';
  }
  
  private String proximoString(final String expresion, final int indice) {
    final int l = expresion.length();
    char c = expresion.charAt(indice);
    int i = indice;
    String resultado = "";
    while (i < l && esLetra(c)) {
      resultado = resultado + c;
      i++;
      if (i < l) {
        c = expresion.charAt(i);
      }
    }
    return resultado;
  }
  
  private String proximoNumero(final String expresion, final int indice) {
    boolean tienePunto = false;
    final int l = expresion.length();
    int i = indice;
    char c = expresion.charAt(i);
    String resultado = "";
    while (i < l && (esDigito(c) || (!tienePunto && esPunto(c)))) {
      resultado = resultado + c;
      if (esPunto(c)) {
        tienePunto = true;
      }
      i++;
      if (i < l) {
        c = expresion.charAt(i);
      }
    }
    return resultado;
  }
  
  public Token proximoToken(final String expresion, final int indice, final boolean menosUnario) {
    // fin del string
    if (indice >= expresion.length()) {
      return new Token(Token.Tipo.NIL, "", 0, 0);
    }
    final char c = expresion.charAt(indice);
    if (esOperador(c)) {
      final String valor;
      if (c == '-') {
        valor = menosUnario
                ? "#"
                : "-";
      } else {
        valor = "" + c;
      }
      return new Token(Token.Tipo.OP, valor, indice, 1);
    }
    if (c == ')') {
      return new Token(Token.Tipo.PAR_D, ")", indice, 1);
    }
    if (c == '(') {
      return new Token(Token.Tipo.PAR_I, "(", indice, 1);
    }
    if (c == ',') {
      return new Token(Token.Tipo.COMA, ",", indice, 1);
    }
    if (esLetra(c)) {
      String valor = proximoString(expresion, indice);
      Token.Tipo tipo = infoOperadores.tokenFuncion(valor);
      return new Token(tipo, valor, indice, valor.length());
    }
    if (esDigito(c) || esPunto(c)) {
      String valor = proximoNumero(expresion, indice);
      return new Token(Token.Tipo.NUM, valor, indice, valor.length());
    }
    return new Token(Token.Tipo.INVALIDO, "" + c, indice, 1);
  }
}
