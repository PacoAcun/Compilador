package compiler.ast;

public class CharType extends Type {
    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
