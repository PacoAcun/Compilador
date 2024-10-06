package compiler;


import compiler.scanner.Scanner;
import compiler.parser.sym;
import compiler.parser.Parser;
import java_cup.runtime.Symbol;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileReader;

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

        try (PrintWriter writer = new PrintWriter(new FileWriter(output))) {
            switch (target) {
                case "scan":
                    runScan(filename, writer, debug);
                    break;
                case "parse":
                    runParse(filename, writer, debug);
                    break;
                default:
                    System.err.println("Objetivo desconocido: " + target);
            }
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo de salida: " + e.getMessage());
        }
    }

    private static void runScan(String filename, PrintWriter writer, boolean debug) throws IOException {
        writer.println("stage: scanning");
        try (FileReader fileReader = new FileReader(filename)) {
            Scanner scanner = new Scanner(fileReader);
            while (!scanner.yyatEOF()) {
                Symbol token = scanner.next_token();
                if (token.sym == sym.EOF) break;

                // Determinar si es palabra reservada
                String tipoToken = esReservada(token.sym) ? "reservada" : "no reservada";

                // Imprimir el token en el formato adecuado
                writer.printf("Token | Valor: %s | Columna: %d | Fila: %d | Tipo: %s%n",
                        token.value, token.right, token.left, tipoToken);

                if (debug) {
                    System.out.printf("Token | Valor: %s | Columna: %d | Fila: %d | Tipo: %s%n",
                            token.value, token.right, token.left, tipoToken);
                }
            }
        }
    }

    private static void runParse(String filename, PrintWriter writer, boolean debug) throws IOException {
        writer.println("stage: parsing");
        try (FileReader fileReader = new FileReader(filename)) {
            Scanner scanner = new Scanner(fileReader);
            Parser parser = new Parser(scanner);
            Symbol result = parser.parse();
            writer.println("Parsing completed successfully.");
            if (debug) {
                System.out.println("Debug: Parsing completed.");
            }
        } catch (Exception e) {
            writer.println("Error during parsing: " + e.getMessage());
        }
    }

    private static boolean esReservada(int tokenSym) {
        switch (tokenSym) {
            case sym.CLASS:
            case sym.INT:
            case sym.VOID:
            case sym.BOOLEAN:
            case sym.TRUE:
            case sym.FALSE:
            case sym.IF:
            case sym.RETURN:
            case sym.WHILE:
            case sym.FOR:
                return true;
            default:
                return false;
        }
    }

    private static void printHelp() {
        System.out.println("Uso: java Compiler [option] <filename>");
        System.out.println("-o <outname>: Especifica el nombre del archivo de salida.");
        System.out.println("-target <stage>: scan, parse.");
        System.out.println("-debug: Activa el modo debug.");
    }
}
