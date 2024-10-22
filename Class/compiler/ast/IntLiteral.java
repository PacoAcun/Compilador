package compiler.ast;

public class IntLiteral extends Literal {
    public int value;

    public IntLiteral(int value) {
        this.value = value;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
