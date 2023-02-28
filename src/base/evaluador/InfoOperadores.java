package base.evaluador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Clase con las tablas de operadores y funciones y métodos para obtener información de operadores.
 */
public class InfoOperadores {
  /**
   * Mapa de prioridad o precedencia de operadores y funciones.
   */
  private static final Map<String, Integer> PRIORIDAD_OPERADORES;
  /**
   * Lista de listerales de funciones predefinidas.
   */
  private static final List<String> FUNCIONES;
  /**
   * Mapa con los lambda para evaluar las funciones y operadores.
   *
   * Una función se debe definir de Double[] -> Doble, de tal forma que se puedan tener varios
   * parámetros de enrtada que se pasan por array, en la evaluación de cada función, esta es
   * responsable de saber cuántos parámetros debe pasar.
   */
  private static final Map<String, Function<Double[], Double>> MAPA_IMPLEMENTACION_FUNCIONES;
  
  static {
    // Se usa # para el menos unario, para distinguir de resta
    PRIORIDAD_OPERADORES = new HashMap<>();
    PRIORIDAD_OPERADORES.put(",", 1);
    PRIORIDAD_OPERADORES.put("-", 2);
    PRIORIDAD_OPERADORES.put("+", 3);
    PRIORIDAD_OPERADORES.put("/", 4);
    PRIORIDAD_OPERADORES.put("*", 5);
    PRIORIDAD_OPERADORES.put("^", 15);
    PRIORIDAD_OPERADORES.put("#", 16);
    PRIORIDAD_OPERADORES.put("sin", 20);
    PRIORIDAD_OPERADORES.put("cos", 20);
    PRIORIDAD_OPERADORES.put("tan", 20);
    PRIORIDAD_OPERADORES.put("asin", 20);
    PRIORIDAD_OPERADORES.put("acos", 20);
    PRIORIDAD_OPERADORES.put("atan", 20);
    PRIORIDAD_OPERADORES.put("exp", 20);
    PRIORIDAD_OPERADORES.put("ln", 20);
    PRIORIDAD_OPERADORES.put("pow", 20);
    PRIORIDAD_OPERADORES.put("max", 20);
    PRIORIDAD_OPERADORES.put("min", 20);
    
    FUNCIONES = new ArrayList<>();
    FUNCIONES.add("sin");
    FUNCIONES.add("cos");
    FUNCIONES.add("tan");
    FUNCIONES.add("asin");
    FUNCIONES.add("acos");
    FUNCIONES.add("atan");
    FUNCIONES.add("exp");
    FUNCIONES.add("ln");
    FUNCIONES.add("pow");
    FUNCIONES.add("max");
    FUNCIONES.add("min");
    
    MAPA_IMPLEMENTACION_FUNCIONES = new HashMap<>();
    MAPA_IMPLEMENTACION_FUNCIONES.put("sin", array -> Math.sin(array[0]));
    MAPA_IMPLEMENTACION_FUNCIONES.put("cos", array -> Math.cos(array[0]));
    MAPA_IMPLEMENTACION_FUNCIONES.put("tan", array -> Math.tan(array[0]));
    MAPA_IMPLEMENTACION_FUNCIONES.put("asin", array -> Math.asin(array[0]));
    MAPA_IMPLEMENTACION_FUNCIONES.put("acos", array -> Math.acos(array[0]));
    MAPA_IMPLEMENTACION_FUNCIONES.put("atan", array -> Math.atan(array[0]));
    MAPA_IMPLEMENTACION_FUNCIONES.put("exp", array -> Math.exp(array[0]));
    MAPA_IMPLEMENTACION_FUNCIONES.put("ln", array -> Math.log(array[0]));
    MAPA_IMPLEMENTACION_FUNCIONES.put("pow", array -> Math.pow(array[0], array[1]));
    MAPA_IMPLEMENTACION_FUNCIONES.put("max", array -> Math.max(array[0], array[1]));
    MAPA_IMPLEMENTACION_FUNCIONES.put("min", array -> Math.min(array[0], array[1]));
    // en el mapa también se agregan las operaciones básicas
    MAPA_IMPLEMENTACION_FUNCIONES.put("+", array -> array[0] + array[1]);
    MAPA_IMPLEMENTACION_FUNCIONES.put("-", array -> array[0] - array[1]);
    MAPA_IMPLEMENTACION_FUNCIONES.put("*", array -> array[0] * array[1]);
    MAPA_IMPLEMENTACION_FUNCIONES.put("/", array -> array[0] / array[1]);
    MAPA_IMPLEMENTACION_FUNCIONES.put("^", array -> Math.pow(array[0], array[1]));
    MAPA_IMPLEMENTACION_FUNCIONES.put("#", array -> -array[0]);
  }
  
  /**
   * Devuelve el tipo de asociatividad de un operador o función
   * @param t El token con el operador o función
   * @return El tipo de asociatividad: 1 = derecha, 0: izquierda.
   */
  public Integer asociatividadOp(Token t) {
    String valor = t.getValor();
    return (t.equals("#") || t.equals("^"))
           ? 1
           : 0;
  }
  
  /**
   * Comparador de la precedencia de dos tokens de operador
   *
   * @param t1 token 1
   * @param t2 token 2
   * @return 0 si son misma precedencia
   * 1 si precedencia1 > precedencia2
   * -1 si precedencia1 < precedencia2
   */
  public int compararPrecedenciaOp(Token t1, Token t2) {
    return PRIORIDAD_OPERADORES.get(t1.getValor())
        .compareTo(PRIORIDAD_OPERADORES.get(t2.getValor()));
  }
  
  /**
   * Dado un literal de string con solo caracteres, se devuelve si correspondes a un token de
   * función o es inválido, consultando en el mapa del nombre de funciones.
   *
   * @param nombreFuncion Valor literal del nombre.
   * @return base.evaluador.Token FUN o INVALIDO según el token sea una función o no.
   */
  public Token.Tipo tokenFuncion(String nombreFuncion) {
    return FUNCIONES.contains(nombreFuncion)
           ? Token.Tipo.FUN
           : Token.Tipo.INVALIDO;
  }
  
  /**
   * Dado un literal string de operador devuelve booleano que indica si es o no booleano.
   *
   * @param operador El literal con el operador a validar
   * @return booleano que indica si el operador es binario (true) o unario (false)
   */
  public boolean esOperadorBinario(String operador) {
    return
        operador.equals("+")
            || operador.equals("-")
            || operador.equals("*")
            || operador.equals("/")
            || operador.equals("^")
            || operador.equals("pow")
            || operador.equals("max")
            || operador.equals("min");
  }
  
  /**
   * Realiza la evaluación de una función del mapa con valores reales.
   *
   * @param nombre Nombre de la función a evaluar.
   * @param parametros Lista opcional de parámetros de tipo doble. Son los parámetros que
   *                   necesita la función específica a evaluar.
   * @return Doble con el resultado de la operación.
   */
  public Double evaluarFuncion(String nombre, Double... parametros) {
    Function<Double[], Double> funcion = MAPA_IMPLEMENTACION_FUNCIONES.get(nombre);
    return funcion == null
           ? null
           : funcion.apply(parametros);
  }
  
  /**
   * Valida si el caracter es una letra minúscula de la a a la z.
   *
   * @param c caracter a evaluar.
   * @return Booleano que indica si el caracter es letra o no.
   */
  public boolean esLetra(final int c) {
    return c >= 'a' && c <= 'z';
  }
  
  /**
   * Valida si el caracter es un dígito.
   *
   * @param c caracter a evaluar.
   * @return Booleano que indica si el caracter es dígito o no.
   */
  public boolean esDigito(final int c) {
    return c >= '0' && c <= '9';
  }
  
  /**
   * Valida si el caracter es un espacio, sea espacio, tabulador o caracteres de salto de línea.
   *
   * @param c caracter a evaluar.
   * @return Booleano que indica si el caracter es espacio o no.
   */
  public boolean esEspacio(final int c) {
    return c == ' ' || c == '\n' || c == '\r' || c == '\t';
  }
  
  /**
   * Valida si el caracter es un salto de línea.
   *
   * @param c caracter a evaluar.
   * @return Booleano que indica si el caracter es salto de línea o no.
   */
  public boolean esSaltoLinea(final int c) {
    return c == '\n';
  }
  
  /**
   * Valida si el caracter es el caracter punto.
   *
   * @param c caracter a evaluar.
   * @return Booleano que indica si el caracter es punto o no.
   */
  public boolean esPunto(final int c) {
    return c == '.';
  }
  
  /**
   * Valida si el caracter es representa un operador aritmético.
   *
   * @param c caracter a evaluar.
   * @return Booleano que indica si el caracter es punto o no.
   */
  public boolean esOperador(final int c) {
    return c == '^' || c == '/' || c == '*' || c == '-' || c == '+';
  }
  
}
