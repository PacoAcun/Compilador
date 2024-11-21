package compiler.irt;

public class JUMP extends IRStmt {
    public IRExp exp;

    public JUMP(IRExp exp) {
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "JUMP " + exp.toString();
    }
}
