package compiler;

import scanner.Scanner;
import parser.sym;
import parser.Parser;  // Importa el parser generado por CUP
import java.io.FileReader;
import java.io.Reader;
import java_cup.runtime.Symbol;
import java_cup.runtime.lr_parser;
import opt.Algebraic;
import opt.ConstantF;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Compiler {
    public static void main(String[] args) {
        System.out.println("Compiler started...");
        System.out.println("Target: " + target);
        System.out.println("Input file: " + inputFileName);


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

            // Fase de análisis léxico (scan)
            if (target.equals("scan")) {
                while ((token = scanner.next_token()).sym != sym.EOF) {
                    String output = "Token: " + token.sym + " | Valor: " + token.value + " | Línea: " + token.left + ", Columna: " + token.right;
                    System.out.println(output);
                    writer.println(output);
                }
                writer.close(); 
                return;
            }

            // Fase de análisis sintáctico (parse)
            if (target.equals("parse") || target.equals("codegen")) {
                System.out.println("Starting parser...");
                Parser parser = new Parser(scanner);  // Instancia del parser
                Symbol parseTree = parser.parse();  // Ejecuta el análisis sintáctico
                System.out.println("Parsing completed successfully.");
                writer.println("Parsing completed successfully.");
            }


            // Optimización Algebraica
            if (target.equals("optimize") || target.equals("codegen")) {
                Algebraic algebraicOptimizer = new Algebraic();
                // ast = algebraicOptimizer.optimize(ast);
                // Ejemplo de cómo se podría optimizar con Algebraic
                // writer.println("Optimización algebraica completada.");
            }

            // Propagación de Constantes
            if (target.equals("optimize") || target.equals("codegen")) {
                ConstantF constantOptimizer = new ConstantF();
                // ast = constantOptimizer.optimize(ast);
                // writer.println("Optimización de constantes completada.");
            }

            // Fase de generación de código
            if (target.equals("codegen")) {
                // CodeGenerator codegen = new CodeGenerator();
                // codegen.generate(ast, writer);
                writer.println("Código generado exitosamente.");
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
