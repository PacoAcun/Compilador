package compiler.semantic;

import java.util.*;

public class SymbolTable {
    private Deque<Map<String, Symbol>> scopes;

    public SymbolTable() {
        scopes = new ArrayDeque<>();
        // Añadir el scope global
        enterScope();
    }

    public void enterScope() {
        scopes.push(new HashMap<>());
    }

    public void exitScope() {
        if (scopes.isEmpty()) {
            throw new EmptyStackException();
        }
        scopes.pop();
    }

    public boolean declare(Symbol symbol) {
        Map<String, Symbol> currentScope = scopes.peek();
        if (currentScope.containsKey(symbol.getName())) {
            return false; // Ya existe en el scope actual
        }
        currentScope.put(symbol.getName(), symbol);
        return true;
    }

    public Symbol lookup(String name) {
        for (Map<String, Symbol> scope : scopes) {
            if (scope.containsKey(name)) {
                return scope.get(name);
            }
        }
        return null; // No encontrado
    }

    public Symbol lookupCurrentScope(String name) {
        Map<String, Symbol> currentScope = scopes.peek();
        return currentScope.get(name);
    }
}
