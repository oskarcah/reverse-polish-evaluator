package base.evaluador;

/**
 * Clase de información de posición de caracter para hallar el próximo token en el String con la
 * expresión infija en las funciones de la clase base.evaluador.ExtractorToken.
 */
public class InfoProximoToken {
  public int caracter = 1;
  public int linea = 1;
  public int indice = 0;
  public boolean menosUnario = true;
}