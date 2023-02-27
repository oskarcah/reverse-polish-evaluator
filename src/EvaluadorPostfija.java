import java.util.List;
import java.util.Stack;

public class EvaluadorPostfija {
  private final InfoOperadores infoOperadores;
  
  public EvaluadorPostfija(InfoOperadores infoOperadores) {
    this.infoOperadores = infoOperadores;
  }
  
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
  
  public double evaluarExpresion(List<Token> expresionPostfija) {
    final Stack<Double> pilaValores = new Stack<>();
    for (final Token t : expresionPostfija) {
      // si el token es número se apila
      // si es operador o función se evalúa
      // si hay otro tipo de token da error
      final Token.Tipo tipo = t.getTipo();
      if (Token.Tipo.NUM == tipo) {
        pilaValores.push(Double.valueOf(t.getValor()));
      } else {
        final String operador = t.getValor();
        // ver si son operadores unarios o binarios
        final double resultadoOperacion;
        if (infoOperadores.esOperadorBinario(operador)) {
          Double op2 = pilaValores.pop();
          Double op1 = pilaValores.pop();
          resultadoOperacion = infoOperadores.evaluarFuncion(operador, op1, op2);
        } else {
          Double op1 = pilaValores.pop();
          resultadoOperacion = infoOperadores.evaluarFuncion(operador, op1);
        }
        pilaValores.push(resultadoOperacion);
      }
    }
    return pilaValores.pop();
  }
}
