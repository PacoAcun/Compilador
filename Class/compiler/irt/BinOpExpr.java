package compiler.irt;

public class BinOpExpr extends IRExp {
    public BinOp op;
    public IRExp left, right;

    public BinOpExpr(BinOp op, IRExp left, IRExp right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "BINOP(" + op.toString() + ", " + left.toString() + ", " + right.toString() + ")";
    }
}
