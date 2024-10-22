package compiler.ast;

public class Param {
    public Type type;
    public String name;
    public boolean isArray;

    public Param(Type type, String name, boolean isArray) {
        this.type = type;
        this.name = name;
        this.isArray = isArray;
    }
}
