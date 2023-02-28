package base.evaluador;

/**
 * Excepción error de parseo en la conversión a postfija
 */
public class ExConversionPostFijo extends RuntimeException {
  public ExConversionPostFijo(InfoProximoToken info, String cause) {
    super("Error de parsing: " + " linea: " + info.linea + ",  caracter: " + info.caracter +
        " causa: " + cause);
  }
}
