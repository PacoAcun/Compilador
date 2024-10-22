package compiler.ast;

public class BooleanType extends Type {
    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
