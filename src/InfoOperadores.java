import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class InfoOperadores {
  
  private static final Map<String, Integer> PRIORIDAD_OPERADORES;
  private static final Map<String, Integer> ASOCIATIVIDAD_OPERADORES;
  private static final List<String> FUNCIONES;
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
    
    // 0 = asociativo por la izquierda
    // 1 = asociativo por la derecha
    ASOCIATIVIDAD_OPERADORES = new HashMap<>();
    ASOCIATIVIDAD_OPERADORES.put("+", 0);
    ASOCIATIVIDAD_OPERADORES.put("-", 0);
    ASOCIATIVIDAD_OPERADORES.put("*", 0);
    ASOCIATIVIDAD_OPERADORES.put("/", 0);
    ASOCIATIVIDAD_OPERADORES.put("^", 1);
    ASOCIATIVIDAD_OPERADORES.put("#", 1);
    
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
  
  public int prioridadToken(Token t) {
    Integer p = InfoOperadores.PRIORIDAD_OPERADORES.get(t.getValor());
    return p == null
           ? 20
           : p;
  }
  
  public int asociatividadOp(Token t) {
    Integer a = InfoOperadores.ASOCIATIVIDAD_OPERADORES.get(t.getValor());
    return a == null
           ? 0
           : a;
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
    final int p1 = prioridadToken(t1);
    final int p2 = prioridadToken(t2);
    if (p1 == p2) {
      return 0;
    }
    if (p1 > p2) {
      return 1;
    }
    return -1;
  }
  
  public Token.Tipo tokenFuncion(String nombreFuncion) {
    return FUNCIONES.contains(nombreFuncion)
           ? Token.Tipo.FUN
           : Token.Tipo.INVALIDO;
  }
  
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
  
  public Double evaluarFuncion(String nombre, Double... parametros) {
    Function<Double[], Double> funcion = MAPA_IMPLEMENTACION_FUNCIONES.get(nombre);
    if (funcion == null) {
      return null;
    }
    return funcion.apply(parametros);
  }
  
}
