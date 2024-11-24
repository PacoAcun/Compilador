package compiler.irt;

import java.util.ArrayList;
import java.util.List;

public class BasicBlock {
    public String label;
    public List<IRStmt> instructions;
    public List<BasicBlock> predecessors; // Añadir este campo
    public List<BasicBlock> successors;   // Añadir este campo
    public List<String> successorLabels;  // Para resolver después las etiquetas de los sucesores

    public BasicBlock() {
        this.instructions = new ArrayList<>();
        this.predecessors = new ArrayList<>();  // Inicializar la lista de predecesores
        this.successors = new ArrayList<>();    // Inicializar la lista de sucesores
        this.successorLabels = new ArrayList<>();
    }

    public void addInstruction(IRStmt stmt) {
        this.instructions.add(stmt);
    }

    
}
