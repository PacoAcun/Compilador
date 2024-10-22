package compiler.ast;

public abstract class Type {
    public abstract void accept(ASTVisitor visitor);
}
