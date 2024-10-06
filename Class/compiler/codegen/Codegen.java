package compiler.codegen;

import compiler.irt.*;;

// Clase Codegen
public class Codegen {
    private Irt irt;

    public Codegen(Irt irt) {
        this.irt = irt;
    }

    public void generateCode(String outputFileName) {
        System.out.println("stage: generating code");
        // Lógica para la generación de código.
    }
}
