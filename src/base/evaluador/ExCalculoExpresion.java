package base.evaluador;

/**
 * Excepción en la evaluación matemática de la expresión postfija
 */
public class ExCalculoExpresion extends RuntimeException {
  public ExCalculoExpresion(String operacion, Throwable cause) {
    super("Operacion inválida: " + operacion + " causa: " + cause);
  }
}
