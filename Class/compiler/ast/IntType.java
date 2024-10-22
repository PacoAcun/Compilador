package compiler.ast;

public class IntType extends Type {
    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
