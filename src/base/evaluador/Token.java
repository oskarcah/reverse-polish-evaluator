package base.evaluador;

/**
 * Representa un token leido de la entrada de datos.
 */
public class Token {
  
  public static enum Tipo {
    NIL, //nulo o fin de linea
    OP, // operador
    FUN, // nombre de función
    NUM, // número
    COMA, // coma
    PAR_I, // paréntesis izquierdo de apertura
    PAR_D, // paréntesis derecho de cierre
    INVALIDO,
  }
  
  private Tipo tipo;
  private String valor;
  private int posInicial;
  private int longitud;
  
  public Token(Tipo tipo, String valor, int posInicial, int longitud) {
    this.tipo = tipo;
    this.valor = valor;
    this.posInicial = posInicial;
    this.longitud = longitud;
  }
  
  public Tipo getTipo() {
    return tipo;
  }
  
  public void setTipo(Tipo tipo) {
    this.tipo = tipo;
  }
  
  public String getValor() {
    return valor;
  }
  
  public void setValor(String valor) {
    this.valor = valor;
  }
  
  public int getPosInicial() {
    return posInicial;
  }
  
  public void setPosInicial(int posInicial) {
    this.posInicial = posInicial;
  }
  
  public int getLongitud() {
    return longitud;
  }
  
  public void setLongitud(int longitud) {
    this.longitud = longitud;
  }
}
