package ast;

public class Declaration {
    // Aquí puedes agregar los atributos necesarios
    private VarDecl varDecl; // Por ejemplo, si solo va a manejar declaraciones de variables

    public Declaration(VarDecl varDecl) {
        this.varDecl = varDecl;
    }

    public VarDecl getVarDecl() {
        return varDecl;
    }

    @Override
    public String toString() {
        return "Declaration{" + "varDecl=" + varDecl + '}';
    }
}
