package compiler;

import compiler.scanner.Scanner;
import compiler.parser.sym;
import compiler.parser.Parser;
import compiler.ast.Program;
import compiler.ast.ASTPrinter;
import java_cup.runtime.Symbol;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileReader;
import java.lang.reflect.Field;

public class Compiler {
    public static void main(String[] args) {
        if (args.length < 1) {
            printHelp();
            System.exit(1);
        }

        String filename = "";
        String output = "output.txt";
        String target = "codegen"; // Por defecto
        boolean debug = false;

        // Procesamiento de argumentos
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-o":
                    if (i + 1 < args.length) {
                        output = args[++i];
                    } else {
                        System.err.println("Error: Se espera un nombre de archivo después de -o.");
                        printHelp();
                        System.exit(1);
                    }
                    break;
                case "-target":
                    if (i + 1 < args.length) {
                        target = args[++i];
                    } else {
                        System.err.println("Error: Se espera un objetivo después de -target.");
                        printHelp();
                        System.exit(1);
                    }
                    break;
                case "-debug":
                    debug = true;
                    break;
                case "-h":
                    printHelp();
                    System.exit(0);
                    break;
                default:
                    filename = args[i];
            }
        }

        if (filename.isEmpty()) {
            System.err.println("Error: No se especificó un archivo de entrada.");
            printHelp();
            System.exit(1);
        }

        try {
            switch (target) {
                case "scan":
                    runScan(filename, output, debug);
                    break;
                case "parse":
                    runParse(filename, output, debug);
                    break;
                default:
                    System.err.println("Objetivo desconocido: " + target);
                    printHelp();
            }
        } catch (IOException e) {
            System.err.println("Error al procesar el archivo: " + e.getMessage());
            if (debug) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Método para ejecutar el análisis sintáctico y generar el AST.
     *
     * @param filename Archivo de entrada.
     * @param output   Archivo de salida.
     * @param debug    Bandera para activar el modo debug.
     * @throws IOException Si ocurre un error de E/S.
     */
    private static void runParse(String filename, String output, boolean debug) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(output))) {
            // Indicar el inicio de la etapa de parsing
            writer.println("stage: parsing");
            System.out.println("stage: parsing");

            // Inicializar Scanner y Parser
            Scanner scanner = new Scanner(new FileReader(filename));
            Parser parser = new Parser(scanner);

            // Realizar el parsing
            Symbol result = parser.parse();

            // Verificar si el parsing resultó en un AST válido
            if (result == null || result.value == null) {
                writer.println("Error: No se pudo generar el AST.");
                if (debug) {
                    System.err.println("Error: No se pudo generar el AST.");
                }
                return;
            }

            // Obtener el nodo raíz del AST
            Program program = (Program) result.value;

            // Confirmar que el parsing fue exitoso
            writer.println("Parsing completed successfully.");
            if (debug) {
                System.out.println("Debug: Parsing completed successfully.");
            }

            // Indicar el inicio de la impresión del AST
            writer.println("AST:");
            ASTPrinter printer = new ASTPrinter(writer);

            // Traversar el AST y generar la representación
            program.accept(printer);
            writer.println(); // Añadir una línea en blanco al final

            if (debug) {
                System.out.println("Debug: AST generado correctamente en " + output);
            }

            // Mensaje de éxito
            System.out.println("Parsing completado exitosamente. AST generado en " + output);
        } catch (Exception e) {
            System.err.println("Error durante el parsing: " + e.getMessage());
            if (debug) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Método para ejecutar el análisis léxico (scanning).
     *
     * @param filename Archivo de entrada.
     * @param output   Archivo de salida.
     * @param debug    Bandera para activar el modo debug.
     * @throws IOException Si ocurre un error de E/S.
     */
    private static void runScan(String filename, String output, boolean debug) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(output))) {
            // Indicar el inicio de la etapa de scanning
            writer.println("stage: scanning");
            System.out.println("stage: scanning");

            try (FileReader fileReader = new FileReader(filename)) {
                Scanner scanner = new Scanner(fileReader);
                while (!scanner.yyatEOF()) {
                    Symbol token = scanner.next_token();
                    if (token.sym == sym.EOF) break;

                    String nombreToken = getTokenName(token.sym);
                    String valorToken = (token.value != null) ? token.value.toString() : "N/A";
                    String tipoToken = esReservada(token.sym) ? "reservada" : "no reservada";

                    writer.printf("Token: %s | Valor: %s | Línea: %d | Columna: %d | Tipo: %s%n",
                            nombreToken, valorToken, token.left + 1, token.right + 1, tipoToken);

                    if (debug) {
                        System.out.printf("Token: %s | Valor: %s | Línea: %d | Columna: %d | Tipo: %s%n",
                                nombreToken, valorToken, token.left + 1, token.right + 1, tipoToken);
                    }
                }
            } catch (Exception e) {
                writer.println("Error durante el análisis de escaneo: " + e.getMessage());
                System.err.println("Error durante el análisis de escaneo: " + e.getMessage());
                if (debug) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Método para determinar si un token es una palabra reservada.
     *
     * @param tokenSym Símbolo del token.
     * @return true si es reservada, false en caso contrario.
     */
    private static boolean esReservada(int tokenSym) {
        switch (tokenSym) {
            case sym.CLASS:
            case sym.INT:
            case sym.BOOLEAN:
            case sym.VOID:
            case sym.TRUE:
            case sym.FALSE:
            case sym.IF:
            case sym.ELSE:
            case sym.FOR:
            case sym.WHILE:
            case sym.RETURN:
            case sym.BREAK:
            case sym.CONTINUE:
            case sym.CALLOUT:
            case sym.CHAR:
            case sym.NEW:
            case sym.ASSIGN:
            case sym.PLUS_ASSIGN:
            case sym.MINUS_ASSIGN:
            case sym.SEMI:
            case sym.COMMA:
            case sym.LBRACE:
            case sym.RBRACE:
            case sym.LPAREN:
            case sym.RPAREN:
            case sym.LBRACKET:
            case sym.RBRACKET:
            case sym.AND:
            case sym.OR:
            case sym.NOT:
            case sym.EQ:
            case sym.NEQ:
            case sym.LE:
            case sym.GE:
            case sym.LT:
            case sym.GT:
            case sym.PLUS:
            case sym.MINUS:
            case sym.TIMES:
            case sym.DIVIDE:
            case sym.MOD:
                return true;
            default:
                return false;
        }
    }

    /**
     * Método para obtener el nombre de un token a partir de su símbolo.
     *
     * @param tokenSym Símbolo del token.
     * @return Nombre del token o "UNKNOWN" si no se encuentra.
     */
    private static String getTokenName(int tokenSym) {
        try {
            Field[] fields = sym.class.getFields();
            for (Field field : fields) {
                if (field.getType() == int.class && field.getInt(null) == tokenSym) {
                    return field.getName();
                }
            }
        } catch (IllegalAccessException e) {
            // Manejar excepción si es necesario
        }
        return "UNKNOWN";
    }

    /**
     * Método para imprimir la ayuda y uso del compilador.
     */
    private static void printHelp() {
        System.out.println("Uso: java compiler.Compiler [option] <filename>");
        System.out.println("-o <outname>: Especifica el nombre del archivo de salida.");
        System.out.println("-target <stage>: scan, parse.");
        System.out.println("-debug: Activa el modo debug.");
        System.out.println("-h: Muestra esta ayuda.");
    }
}
