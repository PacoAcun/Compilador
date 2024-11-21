package compiler.irt;

public class EXPR extends IRStmt {
    public IRExp exp;

    public EXPR(IRExp exp) {
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "EXPR " + exp.toString();
    }
}
