package semantic;

import ast.Ast;

// Clase Semantic
public class Semantic {
    private Ast ast;

    public Semantic(Ast ast) {
        this.ast = ast;
    }

    public void analyze(String outputFileName) {
        System.out.println("stage: semantic analysis");
        // Lógica para el análisis semántico.
    }
}
