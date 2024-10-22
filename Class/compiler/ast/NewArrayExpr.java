// En compiler/ast/NewArrayExpr.java
package compiler.ast;

public class NewArrayExpr extends Expression {
    private Type type;
    private Expression size;

    public NewArrayExpr(Type type, Expression size) {
        this.type = type;
        this.size = size;
    }

    // Métodos getter
    public Type getType() {
        return type;
    }

    public Expression getSize() {
        return size;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
