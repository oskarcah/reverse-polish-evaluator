package base.evaluador;

import java.util.List;
import java.util.Stack;

/**
 * Clase con métodos para mostrar y evaluar una expresión en notación postfija.
 */
public class EvaluadorPostfija {
  private final InfoOperadores infoOperadores;
  
  public EvaluadorPostfija(InfoOperadores infoOperadores) {
    this.infoOperadores = infoOperadores;
  }
  
  /**
   * A partir de una lista de tokens que representa una expresión en postfija se genera el string
   * de esta expresión.
   *
   * @param exprPostfija Lista de tokens con la expresión.
   * @return Representación en string de la expresión.
   */
  public String generarStringExpresion(final List<Token> exprPostfija) {
    // se recorre la lista de tokens de la expresión postfija
    // y se concatenan con espacios
    final StringBuilder resultado = new StringBuilder();
    for (final Token t : exprPostfija) {
      resultado.append(t.getValor())
          .append(' ');
    }
    // se elimina el último espacio adicional
    final int ultimo = resultado.length() - 1;
    resultado.delete(ultimo, ultimo);
    return resultado.toString();
  }
  
  /**
   * Evalúa una expresión en postfija y devuelve el resultado como doble
   *
   * @param expresionPostfija Expresión a evaluar
   * @return valor numérico resultante de la expresión como Double.
   */
  public double evaluarExpresion(List<Token> expresionPostfija) {
    final Stack<Double> pila = new Stack<>();
    for (final Token t : expresionPostfija) {
      // si el token es número se apila
      // si es operador o función se evalúa
      // si hay otro tipo de token da error
      final Token.Tipo tipo = t.getTipo();
      if (Token.Tipo.NUM == tipo) {
        pila.push(Double.valueOf(t.getValor()));
      } else {
        Double op1 = null;
        Double op2 = null;
        String operador = null;
        try {
          operador = t.getValor();
          final double resultadoOperacion;
          
          if (infoOperadores.esOperadorBinario(operador)) {
            // operador binario
            op2 = pila.pop();
            op1 = pila.pop();
            resultadoOperacion = infoOperadores.evaluarFuncion(operador, op1, op2);
          } else {
            // operador unario
            op1 = pila.pop();
            resultadoOperacion = infoOperadores.evaluarFuncion(operador, op1);
          }
          pila.push(resultadoOperacion);
        } catch (Exception e) {
          String mensaje = "Operando1 = " + op1 + ", Operando2=" + op2 + ", operacion=" + operador;
          throw new ExCalculoExpresion("Error realizando calculo: " +  e + ", " + mensaje, e);
        }
      }
    }
    // al final de la evaluación el resultado está en el tope de la pila
    if (pila.empty()) {
      String mensaje =
          "Pila de operandos vacía, la expresión a evaluar está incompleta o tiene errores de sintaxis";
      throw new ExCalculoExpresion("Error realizando calculo: , " + mensaje, null);
    }
    return pila.peek();
  }
}
