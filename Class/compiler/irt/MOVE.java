package compiler.irt;

public class MOVE extends IRStmt {
    public IRExp dst;
    public IRExp src;

    public MOVE(IRExp dst, IRExp src) {
        this.dst = dst;
        this.src = src;
    }

    @Override
    public String toString() {
        return "MOVE " + dst.toString() + ", " + src.toString();
    }
}
