package ast;

import java.util.List;

public class DeclList {
    private List<Declaration> declarations;

    public DeclList(List<Declaration> declarations) {
        this.declarations = declarations;
    }

    public List<Declaration> getDeclarations() {
        return declarations;
    }

    @Override
    public String toString() {
        return "DeclList{" + "declarations=" + declarations + '}';
    }
}
