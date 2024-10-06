package opt;

public class Algebraic {
    // Ejemplo de método para sumar dos números
    public static int sum(int a, int b) {
        return a + b;
    }

    // Ejemplo de método para restar dos números
    public static int subtract(int a, int b) {
        return a - b;
    }

    // Ejemplo de método para multiplicar dos números
    public static int multiply(int a, int b) {
        return a * b;
    }

    // Ejemplo de método para dividir dos números
    public static int divide(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return a / b;
    }

    // Método que agregaste para procesar enteros
    public static String processInteger(String value) {
        // Aquí podrías hacer alguna transformación si fuera necesario
        return value;
    }
}
