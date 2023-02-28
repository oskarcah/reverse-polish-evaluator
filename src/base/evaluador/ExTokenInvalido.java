package base.evaluador;

/**
 * Excepción de token inválido en el parseo de la expresión infija
 */
public class ExTokenInvalido extends RuntimeException {
  public ExTokenInvalido(InfoProximoToken info, String valor) {
    super("Token invalido: " + valor + " linea: " + info.linea + ",  caracter: " + (info.caracter - 1));
  }
}
