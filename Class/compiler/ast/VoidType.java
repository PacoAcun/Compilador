package compiler.ast;

public class VoidType extends Type {
    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
