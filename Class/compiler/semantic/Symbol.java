package compiler.semantic;

import compiler.ast.Type;
import java.util.ArrayList;
import java.util.List;

public class Symbol {
    public enum SymbolType { VARIABLE, METHOD }

    private String name;
    private Type type; // Usar las clases Type del AST
    private SymbolType symbolType;
    private List<Type> parameterTypes; // Para métodos

    public Symbol(String name, Type type, SymbolType symbolType) {
        this.name = name;
        this.type = type;
        this.symbolType = symbolType;
        this.parameterTypes = new ArrayList<>();
    }

    // Getters y setters
    public String getName() { return name; }
    public Type getType() { return type; }
    public SymbolType getSymbolType() { return symbolType; }
    public List<Type> getParameterTypes() { return parameterTypes; }

    public void addParameterType(Type type) {
        parameterTypes.add(type);
    }
}
