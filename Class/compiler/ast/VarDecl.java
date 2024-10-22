package compiler.ast;

public class VarDecl implements ClassBodyMember {
    public Type type;
    public String name;
    public boolean isArray;
    public Expression initExpr;

    public VarDecl(Type type, String name, boolean isArray, Expression initExpr) {
        this.type = type;
        this.name = name;
        this.isArray = isArray;
        this.initExpr = initExpr;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
