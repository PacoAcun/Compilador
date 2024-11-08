package compiler.semantic;

import compiler.ast.*;
import java.util.*;

public class SemanticAnalyzer implements ASTVisitor {
    private SymbolTable symbolTable;
    private List<String> errors;
    private Type currentMethodReturnType; // Para verificar retornos
    private boolean inMainMethod;
    private boolean inLoop; // Para verificar break y continue

    public SemanticAnalyzer() {
        symbolTable = new SymbolTable();
        errors = new ArrayList<>();
        currentMethodReturnType = null;
        inMainMethod = false;
        inLoop = false;
    }

    public List<String> getErrors() {
        return errors;
    }

    // Utilidad para reportar errores
    private void reportError(String message) {
        errors.add(message);
    }

    @Override
    public void visit(Program program) {
        // Insertar métodos y variables globales
        for (ClassBodyMember member : program.classBody) {
            member.accept(this);
        }

        // Verificar que exista el método main sin parámetros
        Symbol mainSymbol = symbolTable.lookup("main");
        if (mainSymbol == null || mainSymbol.getSymbolType() != Symbol.SymbolType.METHOD ||
            mainSymbol.getParameterTypes().size() != 0) {
            reportError("El programa debe contener un método 'main' sin parámetros.");
        }
    }

    @Override
    public void visit(VarDecl varDecl) {
        Type type = varDecl.type;
        String name = varDecl.name;

        Symbol symbol = new Symbol(name, type, Symbol.SymbolType.VARIABLE);

        if (!symbolTable.declare(symbol)) {
            reportError("Identificador '" + name + "' ya está declarado en este scope.");
        }

        if (varDecl.initExpr != null) {
            varDecl.initExpr.accept(this);
            // Verificación de tipos de inicialización
            Type initType = getExpressionType(varDecl.initExpr);
            if (initType != null && !typesAreCompatible(type, initType)) {
                reportError("Tipo de la expresión de inicialización para '" + name + "' no coincide con el tipo declarado.");
            }
        }
    }

    @Override
    public void visit(MethodDecl methodDecl) {
        String methodName = methodDecl.name;
        Type returnType = methodDecl.returnType;

        Symbol methodSymbol = new Symbol(methodName, returnType, Symbol.SymbolType.METHOD);
        for (Param param : methodDecl.params) {
            methodSymbol.addParameterType(param.type);
        }

        if (!symbolTable.declare(methodSymbol)) {
            reportError("Método '" + methodName + "' ya está declarado.");
        }

        // Manejar el scope del método
        symbolTable.enterScope();
        // Insertar parámetros en el scope del método
        for (Param param : methodDecl.params) {
            Symbol paramSymbol = new Symbol(param.name, param.type, Symbol.SymbolType.VARIABLE);
            if (!symbolTable.declare(paramSymbol)) {
                reportError("Parámetro '" + param.name + "' ya está declarado en este método.");
            }
        }

        // Manejar el retorno
        Type previousReturnType = currentMethodReturnType;
        currentMethodReturnType = returnType;

        // Verificar si es el método main
        if (methodName.equals("main")) {
            if (methodDecl.params.size() != 0) {
                reportError("El método 'main' debe tener cero parámetros.");
            }
            inMainMethod = true;
        }

        // Visitar el cuerpo del método
        methodDecl.body.accept(this);

        // Restaurar el retorno anterior
        currentMethodReturnType = previousReturnType;

        // Salir del scope del método
        symbolTable.exitScope();
    }

    @Override
    public void visit(Block block) {
        // Manejar el scope del bloque
        symbolTable.enterScope();

        // Declaraciones de variables
        for (VarDecl varDecl : block.varDecls) {
            varDecl.accept(this);
        }

        // Sentencias
        for (Statement stmt : block.statements) {
            stmt.accept(this);
        }

        symbolTable.exitScope();
    }

    @Override
    public void visit(VarDeclStmt varDeclStmt) {
        varDeclStmt.getVarDecl().accept(this);
        if (varDeclStmt.getInitExpression() != null) {
            varDeclStmt.getInitExpression().accept(this);
            // Verificar que el tipo de la expresión coincida con el tipo de la variable
            Type varType = varDeclStmt.getVarDecl().type;
            Type exprType = getExpressionType(varDeclStmt.getInitExpression());
            if (exprType != null && !typesAreCompatible(varType, exprType)) {
                reportError("Tipo de la expresión de inicialización en declaración de variable no coincide con el tipo declarado.");
            }
        }
    }

    @Override
    public void visit(AssignStmt assignStmt) {
        // Verificar que la variable esté declarada
        assignStmt.location.accept(this);
        assignStmt.expr.accept(this);
        // Verificar que los tipos de location y expr coincidan
        Type locType = getExpressionType(assignStmt.location);
        Type exprType = getExpressionType(assignStmt.expr);
        String op = assignStmt.op;

        if (op.equals("=")) {
            if (locType == null || exprType == null || !typesAreCompatible(locType, exprType)) {
                reportError("Tipos incompatibles en asignación: " + locType + " y " + exprType + ".");
            }
        } else if (op.equals("+=") || op.equals("-=")) {
            if (!(locType instanceof IntType) || !(exprType instanceof IntType)) {
                reportError("Los operandos de '" + op + "' deben ser de tipo int.");
            }
        }
    }

    @Override
    public void visit(IfStmt ifStmt) {
        ifStmt.getCondition().accept(this);
        // Verificar que la condición sea booleana
        Type condType = getExpressionType(ifStmt.getCondition());
        if (!(condType instanceof BooleanType)) {
            reportError("La condición del 'if' debe ser de tipo boolean.");
        }
        // Luego visitar los bloques
        ifStmt.getThenBlock().accept(this);
        if (ifStmt.getElseBlock() != null) {
            ifStmt.getElseBlock().accept(this);
        }
    }

    @Override
    public void visit(MultiVarDecl multiVarDecl) {
        // Itera sobre cada declaración de variable y realiza las verificaciones necesarias
        for (ClassBodyMember decl : multiVarDecl.getDeclarations()) {
            decl.accept(this);
        }
    }


    @Override
    public void visit(ForStmt forStmt) {
        // Implementa las verificaciones semánticas para ForStmt
        // Por ejemplo:
        forStmt.getInit().accept(this);
        forStmt.getCondition().accept(this);
        Type condType = getExpressionType(forStmt.getCondition());
        if (!(condType instanceof IntType)) {
            reportError("La condición del 'for' debe ser de tipo int.");
        }
        forStmt.getUpdate().accept(this);
        forStmt.getBody().accept(this);
    }

    @Override
    public void visit(WhileStmt whileStmt) {
        whileStmt.getCondition().accept(this);
        // Verificar que la condición sea booleana (Regla 11)
        Type condType = getExpressionType(whileStmt.getCondition());
        if (!(condType instanceof BooleanType)) {
            reportError("La condición del 'while' debe ser de tipo boolean.");
        }
        // Visitar el cuerpo
        inLoop = true;
        whileStmt.getBody().accept(this);
        inLoop = false;
    }

    @Override
    public void visit(ExprArg exprArg) {
        // Implementa las verificaciones semánticas necesarias para ExprArg
        // Por ejemplo, verificar el tipo de la expresión
        exprArg.getExpression().accept(this);
    }


    @Override
    public void visit(ReturnStmt returnStmt) {
        if (currentMethodReturnType == null) {
            reportError("La sentencia 'return' está fuera de un método.");
            return;
        }

        if (returnStmt.getExpression() == null) {
            if (!(currentMethodReturnType instanceof VoidType)) {
                reportError("Se esperaba un valor de retorno en el método.");
            }
        } else {
            returnStmt.getExpression().accept(this);
            // Verificar que el tipo de la expresión coincide con el tipo de retorno del método (Regla 8)
            Type exprType = getExpressionType(returnStmt.getExpression());
            if (currentMethodReturnType == null || exprType == null || !typesAreCompatible(currentMethodReturnType, exprType)) {
                reportError("La expresión de retorno debe ser de tipo " + currentMethodReturnType + ", pero es de tipo " + exprType + ".");
            }
            if (currentMethodReturnType instanceof VoidType) {
                reportError("El método es 'void' y no debe retornar un valor.");
            }
        }
    }

    @Override
    public void visit(BreakStmt breakStmt) {
        if (!inLoop) {
            reportError("La sentencia 'break' debe estar dentro de un ciclo.");
        }
    }

    @Override
    public void visit(ContinueStmt continueStmt) {
        if (!inLoop) {
            reportError("La sentencia 'continue' debe estar dentro de un ciclo.");
        }
    }

    @Override
    public void visit(CalloutStmt calloutStmt) {
        calloutStmt.getCalloutCall().accept(this);
        // Implementar verificaciones específicas para callouts si es necesario
    }

    @Override
    public void visit(StringArg stringArg) {
        // No se necesita acción para StringArg en el análisis semántico
    }

    @Override
    public void visit(ExprStmt exprStmt) {
        exprStmt.getExpression().accept(this);
    }

    @Override
    public void visit(AssignExpr assignExpr) {
        assignExpr.getLocation().accept(this);
        assignExpr.getExpression().accept(this);

        Type locType = getExpressionType(assignExpr.getLocation());
        Type exprType = getExpressionType(assignExpr.getExpression());

        String op = assignExpr.getOperator();

        if (op.equals("=")) {
            if (locType == null || exprType == null || !typesAreCompatible(locType, exprType)) {
                reportError("Tipos incompatibles en asignación: " + locType + " y " + exprType + ".");
            }
        } else if (op.equals("+=") || op.equals("-=")) {
            if (!(locType instanceof IntType) || !(exprType instanceof IntType)) {
                reportError("Los operandos de '" + op + "' deben ser de tipo int.");
            }
        }
    }

    @Override
    public void visit(BinaryExpr binaryExpr) {
        binaryExpr.left.accept(this);
        binaryExpr.right.accept(this);

        Type leftType = getExpressionType(binaryExpr.left);
        Type rightType = getExpressionType(binaryExpr.right);

        String op = binaryExpr.op;

        if (isArithmeticOp(op) || isRelationalOp(op)) {
            if (!(leftType instanceof IntType) || !(rightType instanceof IntType)) {
                reportError("Los operandos del operador '" + op + "' deben ser de tipo int.");
            }
        } else if (isEqualityOp(op)) {
            if (leftType == null || rightType == null || !leftType.equals(rightType)) {
                reportError("Los operandos del operador '" + op + "' deben ser del mismo tipo.");
            }
        } else if (isConditionalOp(op)) {
            if (!(leftType instanceof BooleanType) || !(rightType instanceof BooleanType)) {
                reportError("Los operandos del operador '" + op + "' deben ser de tipo boolean.");
            }
        }
    }

    @Override
    public void visit(UnaryExpr unaryExpr) {
        unaryExpr.expr.accept(this);
        Type exprType = getExpressionType(unaryExpr.expr);
        String op = unaryExpr.op;

        if (op.equals("!")) {
            if (!(exprType instanceof BooleanType)) {
                reportError("El operando del operador '!' debe ser de tipo boolean.");
            }
        }
        // Manejar otros operadores unarios si existen
    }

    @Override
    public void visit(MethodCall methodCall) {
        Symbol methodSymbol = symbolTable.lookup(methodCall.getMethodName());
        if (methodSymbol == null || methodSymbol.getSymbolType() != Symbol.SymbolType.METHOD) {
            reportError("Método '" + methodCall.getMethodName() + "' no está declarado.");
            return;
        }

        List<Expression> args = methodCall.getArguments();
        List<Type> paramTypes = methodSymbol.getParameterTypes();

        if (args.size() != paramTypes.size()) {
            reportError("El método '" + methodCall.getMethodName() + "' espera " + paramTypes.size() + " argumentos, pero se proporcionaron " + args.size() + ".");
            return;
        }

        for (int i = 0; i < args.size(); i++) {
            Expression arg = args.get(i);
            Type expectedType = paramTypes.get(i);
            Type actualType = getExpressionType(arg);
            if (actualType == null || !typesAreCompatible(expectedType, actualType)) {
                reportError("Tipo de argumento " + (i + 1) + " en llamada a '" + methodCall.getMethodName() + "' no coincide. Se esperaba " + expectedType + " pero se obtuvo " + actualType + ".");
            }
        }

        // Regla 6: Si un método es usado como expresión, debe retornar un valor
        // Esta verificación dependerá del contexto en que se use el método
        // Puedes implementar lógica adicional para rastrear el contexto de uso
    }

    @Override
    public void visit(CalloutCall calloutCall) {
        // Implementar verificaciones específicas para callouts si es necesario
    }

    @Override
    public void visit(NewArrayExpr newArrayExpr) {
        newArrayExpr.getType().accept(this);
        newArrayExpr.getSize().accept(this);
        // Verificar que el tamaño sea un entero mayor que 0 (Regla 4)
        if (!isConstantInt(newArrayExpr.getSize(), 0)) {
            reportError("El tamaño del arreglo debe ser un entero mayor que 0.");
        }
    }

    @Override
    public void visit(VarLocation varLocation) {
        Symbol symbol = symbolTable.lookup(varLocation.name);
        if (symbol == null || symbol.getSymbolType() != Symbol.SymbolType.VARIABLE) {
            reportError("La variable '" + varLocation.name + "' no está declarada.");
        }
    }

    @Override
    public void visit(ArrayLocation arrayLocation) {
        Symbol symbol = symbolTable.lookup(arrayLocation.name);
        if (symbol == null) {
            reportError("El arreglo '" + arrayLocation.name + "' no está declarado.");
            return;
        }
        if (!(symbol.getType() instanceof ArrayType)) {
            reportError("La variable '" + arrayLocation.name + "' no es un arreglo.");
        }

        arrayLocation.index.accept(this);
        // Verificar que el índice sea de tipo int (Regla 10b)
        Type indexType = getExpressionType(arrayLocation.index);
        if (!(indexType instanceof IntType)) {
            reportError("El índice del arreglo '" + arrayLocation.name + "' debe ser de tipo int.");
        }
    }

    @Override
    public void visit(IntType intType) {
        // No se necesita acción
    }

    @Override
    public void visit(BooleanType booleanType) {
        // No se necesita acción
    }

    @Override
    public void visit(CharType charType) {
        // No se necesita acción
    }

    @Override
    public void visit(VoidType voidType) {
        // No se necesita acción
    }

    @Override
    public void visit(IntLiteral intLiteral) {
        // No se necesita acción
    }

    @Override
    public void visit(BoolLiteral boolLiteral) {
        // No se necesita acción
    }

    @Override
    public void visit(CharLiteral charLiteral) {
        // No se necesita acción
    }

    @Override
    public void visit(StringLiteral stringLiteral) {
        // Si Decaf soporta strings, retornar el tipo correspondiente
        // De lo contrario, podrías reportar un error
    }

    @Override
    public void visit(ArrayType arrayType) {
        // No se necesita acción
    }

    @Override
    public void visit(Param param) {
        // No se necesita acción
    }

    @Override
    public void visit(MethodCallStmt methodCallStmt) {
        methodCallStmt.getMethodCall().accept(this);
        // Verificar que el método existe y los argumentos coinciden con su firma (Regla 5)
        Symbol methodSymbol = symbolTable.lookup(methodCallStmt.getMethodCall().getMethodName());
        if (methodSymbol == null || methodSymbol.getSymbolType() != Symbol.SymbolType.METHOD) {
            reportError("Método '" + methodCallStmt.getMethodCall().getMethodName() + "' no está declarado.");
            return;
        }

        List<Expression> args = methodCallStmt.getMethodCall().getArguments();
        List<Type> paramTypes = methodSymbol.getParameterTypes();

        if (args.size() != paramTypes.size()) {
            reportError("El método '" + methodCallStmt.getMethodCall().getMethodName() + "' espera " + paramTypes.size() + " argumentos, pero se proporcionaron " + args.size() + ".");
            return;
        }

        for (int i = 0; i < args.size(); i++) {
            Expression arg = args.get(i);
            Type expectedType = paramTypes.get(i);
            Type actualType = getExpressionType(arg);
            if (actualType == null || !typesAreCompatible(expectedType, actualType)) {
                reportError("Tipo de argumento " + (i + 1) + " en llamada a '" + methodCallStmt.getMethodCall().getMethodName() + "' no coincide. Se esperaba " + expectedType + " pero se obtuvo " + actualType + ".");
            }
        }
    }

    // Métodos auxiliares
    private boolean isArithmeticOp(String op) {
        return op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/");
    }

    private boolean isRelationalOp(String op) {
        return op.equals("<") || op.equals("<=") || op.equals(">") || op.equals(">=");
    }

    private boolean isEqualityOp(String op) {
        return op.equals("==") || op.equals("!=");
    }

    private boolean isConditionalOp(String op) {
        return op.equals("&&") || op.equals("||");
    }

    private boolean isConstantInt(Expression expr, int minValue) {
        if (expr instanceof IntLiteral) {
            IntLiteral intLit = (IntLiteral) expr;
            return intLit.value > minValue;
        }
        return false; // No es una constante o no cumple
    }

    private boolean typesAreCompatible(Type expected, Type actual) {
        if (expected == null || actual == null) {
            return false;
        }
        return expected.equals(actual);
    }

    private Type getExpressionType(Expression expr) {
        if (expr instanceof IntLiteral) {
            return new IntType();
        } else if (expr instanceof BoolLiteral) {
            return new BooleanType();
        } else if (expr instanceof CharLiteral) {
            return new CharType();
        } else if (expr instanceof StringLiteral) {
            // Si Decaf soporta strings, retornar el tipo correspondiente
            // De lo contrario, podrías reportar un error
            return null;
        } else if (expr instanceof VarLocation) {
            Symbol symbol = symbolTable.lookup(((VarLocation) expr).name);
            if (symbol != null) {
                return symbol.getType();
            }
        } else if (expr instanceof ArrayLocation) {
            Symbol symbol = symbolTable.lookup(((ArrayLocation) expr).name);
            if (symbol != null && symbol.getType() instanceof ArrayType) {
                return ((ArrayType) symbol.getType()).getElementType();
            }
        } else if (expr instanceof BinaryExpr) {
            BinaryExpr binExpr = (BinaryExpr) expr;
            String op = binExpr.op;
            if (isArithmeticOp(op) || isRelationalOp(op)) {
                return new IntType();
            } else if (isEqualityOp(op)) {
                return new BooleanType();
            } else if (isConditionalOp(op)) {
                return new BooleanType();
            }
        } else if (expr instanceof UnaryExpr) {
            UnaryExpr unExpr = (UnaryExpr) expr;
            if (unExpr.op.equals("!")) {
                return new BooleanType();
            }
        } else if (expr instanceof MethodCall) {
            Symbol methodSymbol = symbolTable.lookup(((MethodCall) expr).getMethodName());
            if (methodSymbol != null) {
                return methodSymbol.getType();
            }
        } else if (expr instanceof NewArrayExpr) {
            return ((NewArrayExpr) expr).getType();
        }
        // Manejar otros tipos de expresiones según tu AST
        return null; // Por defecto
    }
}
