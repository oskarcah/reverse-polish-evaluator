import java.util.List;

public class Main {
  // programa principal
  public static void main(String[] args) {
    final InfoOperadores infoOperadores = new InfoOperadores();
    final ExtractorToken extractorToken = new ExtractorToken(infoOperadores);
    ConvertidorPostfija conv = new ConvertidorPostfija(infoOperadores, extractorToken);
    EvaluadorPostfija eval = new EvaluadorPostfija(infoOperadores);
    
    List<Token> resultado = conv.convertirExpresion("max(max(8,3),max(min(20+5,3*24),1))");
    System.out.println("Expresión en postfijo: " + eval.generarStringExpresion(resultado));
    System.out.println("Resultado de evaluación: " + eval.evaluarExpresion(resultado));
  
  }
}