package ast;

import parser.Parser;

// Clase AST
public class Ast {
    private Parser parser;

    public Ast(Parser parser) {
        this.parser = parser;
    }

    public void generateAst(String outputFileName) {
        System.out.println("stage: generating AST");
        // Lógica para generar el AST.
    }
}
