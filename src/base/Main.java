package base;

import base.evaluador.ConvertidorPostfija;
import base.evaluador.EvaluadorPostfija;
import base.evaluador.ExtractorToken;
import base.evaluador.InfoOperadores;
import base.evaluador.Token;
import java.util.List;
import java.util.Scanner;

/*
* Clase con el programa principal.
* */
public class Main {
  // programa principal
  public static void main(String[] args) {
    final InfoOperadores infoOperadores = new InfoOperadores();
    final ExtractorToken extractorToken = new ExtractorToken(infoOperadores);
    ConvertidorPostfija conv = new ConvertidorPostfija(infoOperadores, extractorToken);
    EvaluadorPostfija eval = new EvaluadorPostfija(infoOperadores);
  
    System.out.println("CONVERTIDOR DE EXPRESIONES INFIJAS A POSTFIJAS");
    System.out.println("-----------------------------------------------");
    System.out.println("Operaciones permitidas:");
    System.out.println(
        "+ (suma), - (resta), * (producto), / (división), ^ (potencia entero positivo)");
    System.out.println("Funciones permitidas:");
    System.out.println("sin(x) seno, cos(x) coseno, tan(x) tangente, x en radianes");
    System.out.println("asin(x) arcoseno, acos(x) arcocoseno, tan(x) arcotangente, en radianes");
    System.out.println("pow(x,y) potencia, max(x,y) máximo, min(x,y) mínimo");
    System.out.println("-----------------------------------------------");
    System.out.println("Ingrese la expresion en notacion infija:");
    Scanner scanner = new Scanner(System.in);
    String linea = scanner.nextLine();
    if (linea == null) {
      System.out.println("Se cierra el programa");
      System.exit(1);
      return;
    }
    
    // convertir a postfija
    try {
      List<Token> postFija = conv.convertirExpresion(linea.trim());
      System.out.println("La expresión en postfija es:");
      System.out.println(eval.generarStringExpresion(postFija));
      System.out.println();
      System.out.println("El valor de la expresión es:");
      System.out.println(eval.evaluarExpresion(postFija));
      System.exit(0);
    } catch (Exception e) {
      System.out.println("Ocurrió un error evaluando la expresión:");
      System.out.println("Excepcion: " + e);
      System.out.println("Mensaje: " + e.getMessage());
      System.exit(1);
    }
  }
}