package compiler.irt;

public class LABEL extends IRStmt {
    public String label;

    public LABEL(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "LABEL " + label;
    }
}
