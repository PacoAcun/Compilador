package compiler;

import scanner.Scanner;
import parser.sym;
import java.io.FileReader;
import java.io.Reader;
import java_cup.runtime.Symbol;
import opt.Algebraic;
import opt.ConstantF;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Compiler {
    public static void main(String[] args) {
        System.out.println("Compiler started...");

        if (args.length == 0 || args[0].equals("-h")) {
            printHelp();
            return;
        }

        String inputFileName = null;
        String outputFileName = "output.txt";
        String target = "codegen";  // Default target is codegen

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-o":
                    outputFileName = args[++i];
                    break;
                case "-target":
                    target = args[++i];
                    break;
                default:
                    inputFileName = args[i];
                    break;
            }
        }

        if (inputFileName == null) {
            printHelp();
            return;
        }

        try {
            Reader reader = new FileReader(inputFileName);
            Scanner scanner = new Scanner(reader);
            Symbol token;

            PrintWriter writer = new PrintWriter(new FileWriter(outputFileName));

            if (target.equals("scan")) {
                while ((token = scanner.next_token()).sym != sym.EOF) {
                    String output = "Token: " + token.sym + " | Valor: " + token.value + " | Línea: " + token.left + ", Columna: " + token.right;
                    System.out.println(output);
                    writer.println(output);
                }
                writer.close(); 
                return;
            }

            // Fase de análisis sintáctico
            // Aquí llamas a tu parser para obtener el AST
            // Parser parser = new Parser(scanner);
            // ASTNode ast = parser.parse();

            // Optimización Algebraica
            Algebraic algebraicOptimizer = new Algebraic();
            // ast = algebraicOptimizer.optimize(ast);

            // Propagación de Constantes
            ConstantF constantOptimizer = new ConstantF();
            // ast = constantOptimizer.optimize(ast);

            // Continua con la generación de código o cualquier otro target
            if (target.equals("codegen")) {
                // Genera el código usando el AST optimizado
                // CodeGenerator codegen = new CodeGenerator();
                // codegen.generate(ast, writer);
            }

            writer.close(); // Cierra el escritor si continúa a otras fases

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printHelp() {
        System.out.println("Usage: java compiler.Compiler [options] <filename>");
        System.out.println("Options:");
        System.out.println("-o <outname>    Write output to <outname>");
        System.out.println("-target <stage> Compile up to <stage> stage (scan, parse, ast, etc.)");
    }
}
