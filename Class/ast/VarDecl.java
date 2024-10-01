package ast;

public class VarDecl {
    private String type;
    private String identifier;

    public VarDecl(String type, String identifier) {
        this.type = type;
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return "VarDecl{" + "type='" + type + '\'' + ", identifier='" + identifier + '\'' + '}';
    }
}
