package compiler;

import compiler.scanner.Scanner;
import compiler.parser.sym;
import compiler.parser.Parser;
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
                    output = args[++i];
                    break;
                case "-target":
                    target = args[++i];
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

        // Validar que se haya especificado un archivo de entrada
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
            }
        } catch (IOException e) {
            System.err.println("Error al procesar el archivo: " + e.getMessage());
        }
    }

    private static void runScan(String filename, String output, boolean debug) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(output))) {
            writer.println("stage: scanning");
            try (FileReader fileReader = new FileReader(filename)) {
                Scanner scanner = new Scanner(fileReader);
                while (!scanner.yyatEOF()) {
                    Symbol token = scanner.next_token();
                    if (token.sym == sym.EOF) break;

                    // Obtener el nombre del token
                    String nombreToken = getTokenName(token.sym);

                    // Obtener el valor del token
                    String valorToken = (token.value != null) ? token.value.toString() : "N/A";

                    // Determinar si el token es una palabra reservada o no
                    String tipoToken = esReservada(token.sym) ? "reservada" : "no reservada";

                    // Imprimir el token en el formato adecuado
                    writer.printf("Token: %s | Valor: %s | Línea: %d | Columna: %d | Tipo: %s%n",
                            nombreToken, valorToken, token.left + 1, token.right + 1, tipoToken);

                    if (debug) {
                        System.out.printf("Token: %s | Valor: %s | Línea: %d | Columna: %d | Tipo: %s%n",
                                nombreToken, valorToken, token.left + 1, token.right + 1, tipoToken);
                    }
                }
            } catch (Exception e) {
                writer.println("Error durante el análisis de escaneo: " + e.getMessage());
                if (debug) {
                    System.err.println("Error durante el análisis de escaneo: " + e.getMessage());
                }
            }
        }
    }

    private static void runParse(String filename, String output, boolean debug) throws IOException {
        // Limpiar el archivo de salida antes de empezar
        try (PrintWriter writer = new PrintWriter(new FileWriter(output))) {
            writer.println("stage: parsing");
        }

        try {
            Scanner scanner = new Scanner(new FileReader(filename));
            Parser parser = new Parser(scanner);

            // Si es necesario, puedes pasar el nombre del archivo de salida al parser
            // parser.setOutputFile(output);

            Symbol result = parser.parse();

            // Si el parsing se completó sin excepciones, escribimos un mensaje de éxito
            try (PrintWriter writer = new PrintWriter(new FileWriter(output, true))) {
                writer.println("Parsing completed successfully.");
            }

            if (debug) {
                System.out.println("Debug: Parsing completed successfully.");
            }
        } catch (Exception e) {
            // Los errores de sintaxis se manejan en el parser y se escriben en el archivo de salida
            if (debug) {
                System.err.println("Debug: Error durante el parsing: " + e.getMessage());
            }
        }
    }

   private static boolean esReservada(int tokenSym) {
    // Lista actualizada de tokens que son palabras reservadas o símbolos
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

    private static void printHelp() {
        System.out.println("Uso: java Compiler [option] <filename>");
        System.out.println("-o <outname>: Especifica el nombre del archivo de salida.");
        System.out.println("-target <stage>: scan, parse.");
        System.out.println("-debug: Activa el modo debug.");
    }
}