package compiler.semantic;

import compiler.ast.*;
import java.util.*;

public class SemanticAnalyzer implements ASTVisitor {
    private SymbolTable symbolTable;
    private List<String> errors;
    private Type currentMethodReturnType;
    private boolean inMainMethod;
    private boolean inLoop;
    private Set<String> currentCallStack;
    private boolean hasReturnStatement;
    private String currentMethodName;
    private int loopDepth;

    public SemanticAnalyzer() {
        symbolTable = new SymbolTable();
        errors = new ArrayList<>();
        currentMethodReturnType = null;
        inMainMethod = false;
        inLoop = false;
        currentCallStack = new HashSet<>();
        hasReturnStatement = false;
        currentMethodName = "";
        loopDepth = 0;
    }

    public List<String> getErrors() {
        return errors;
    }

    private void reportError(String message) {
        errors.add(message);
    }

    private void reportError(String message, int line) {
        errors.add(String.format("Error en línea %d: %s", line, message));
    }

    private void checkMainMethodExists(Program program) {
        boolean hasMainMethod = false;
        boolean hasValidMainMethod = false;
        
        for (ClassBodyMember member : program.classBody) {
            if (member instanceof MethodDecl) {
                MethodDecl methodDecl = (MethodDecl) member;
                if (methodDecl.name.equals("main")) {
                    hasMainMethod = true;
                    if (methodDecl.returnType instanceof VoidType && methodDecl.params.isEmpty()) {
                        hasValidMainMethod = true;
                    }
                    break;
                }
            }
        }
        
        if (!hasMainMethod) {
            reportError("El programa debe contener un método 'main'.");
        } else if (!hasValidMainMethod) {
            reportError("El método 'main' debe ser void y no tener parámetros.");
        }
    }

    @Override
    public void visit(Program program) {
        System.out.println("Declarando variables globales...");
        
        // Primera pasada: declarar variables globales
        for (ClassBodyMember member : program.classBody) {
            if (member instanceof VarDecl || member instanceof MultiVarDecl) {
                member.accept(this);
            }
        }

        System.out.println("Contenido de la tabla de símbolos después de declarar variables globales:");
        symbolTable.printAllScopes();

        // Segunda pasada: declarar métodos
        System.out.println("Declarando métodos...");
        for (ClassBodyMember member : program.classBody) {
            if (member instanceof MethodDecl) {
                MethodDecl methodDecl = (MethodDecl) member;
                declareMethod(methodDecl);
            }
        }

        // Verificar existencia del método main
        checkMainMethodExists(program);

        // Tercera pasada: analizar cuerpos de métodos
        System.out.println("Analizando métodos...");
        for (ClassBodyMember member : program.classBody) {
            if (member instanceof MethodDecl) {
                member.accept(this);
            }
        }
    }

    private void declareMethod(MethodDecl methodDecl) {
        String methodName = methodDecl.name;
        Type returnType = methodDecl.returnType;

        Symbol methodSymbol = new Symbol(methodName, returnType, Symbol.SymbolType.METHOD);
        for (Param param : methodDecl.params) {
            methodSymbol.addParameterType(param.type);
        }

        if (!symbolTable.declare(methodSymbol)) {
            reportError("Método '" + methodName + "' ya está declarado.");
        }
    }

    @Override
    public void visit(VarDecl varDecl) {
        Type type = varDecl.type;
        String name = varDecl.name;

        System.out.println("visit(VarDecl): Declarando variable '" + name + "' de tipo '" + type + "'.");

        Symbol symbol = new Symbol(name, type, Symbol.SymbolType.VARIABLE);

        if (!symbolTable.declare(symbol)) {
            reportError("Identificador '" + name + "' ya está declarado en este scope.");
        } else {
            System.out.println("Variable '" + name + "' declarada en el scope actual.");
        }

        if (varDecl.initExpr != null) {
            varDecl.initExpr.accept(this);
            Type initType = getExpressionType(varDecl.initExpr);
            if (initType != null && !typesAreCompatible(type, initType)) {
                reportError("Tipo de la expresión de inicialización para '" + name + 
                          "' no coincide con el tipo declarado. Se esperaba " + type + 
                          " pero se encontró " + initType + ".");
            }
        }
        
        // Si es un array, verificar que el tamaño sea positivo
        if (type instanceof ArrayType && varDecl.initExpr instanceof NewArrayExpr) {
            NewArrayExpr arrayExpr = (NewArrayExpr) varDecl.initExpr;
            if (arrayExpr.getSize() instanceof IntLiteral) {
                int size = ((IntLiteral) arrayExpr.getSize()).getValue();
                if (size <= 0) {
                    reportError("El tamaño del array debe ser mayor que cero.");
                }
            }
        }
    }

    @Override
    public void visit(MethodDecl methodDecl) {
        String methodName = methodDecl.name;
        currentMethodName = methodName;
        Type returnType = methodDecl.returnType;
        hasReturnStatement = false;

        symbolTable.enterScope();
        System.out.println("Entrando al scope del método '" + methodName + "'.");

        // Insertar parámetros
        for (Param param : methodDecl.params) {
            Symbol paramSymbol = new Symbol(param.name, param.type, Symbol.SymbolType.VARIABLE);
            if (!symbolTable.declare(paramSymbol)) {
                reportError("Parámetro '" + param.name + "' ya está declarado en este método.");
            }
        }

        Type previousReturnType = currentMethodReturnType;
        currentMethodReturnType = returnType;

        boolean previousInMainMethod = inMainMethod;
        if (methodName.equals("main")) {
            inMainMethod = true;
        }

        // Visitar el cuerpo
        methodDecl.body.accept(this);

        // Verificar que los métodos no void tengan return en todos los caminos
        if (!(returnType instanceof VoidType) && !hasReturnStatement) {
            reportError("El método '" + methodName + "' debe retornar un valor en todos los caminos de ejecución.");
        }

        currentMethodReturnType = previousReturnType;
        inMainMethod = previousInMainMethod;
        currentMethodName = "";

        symbolTable.exitScope();
        System.out.println("Saliendo del scope del método '" + methodName + "'.");
    }

    @Override
    public void visit(Block block) {
        symbolTable.enterScope();
        System.out.println("Entrando a un nuevo scope de bloque.");

        for (VarDecl varDecl : block.varDecls) {
            varDecl.accept(this);
        }

        for (Statement stmt : block.statements) {
            stmt.accept(this);
        }

        symbolTable.exitScope();
        System.out.println("Saliendo del scope de bloque.");
    }

    @Override
    public void visit(AssignStmt assignStmt) {
        assignStmt.location.accept(this);
        assignStmt.expr.accept(this);

        Type locType = getExpressionType(assignStmt.location);
        Type exprType = getExpressionType(assignStmt.expr);
        String op = assignStmt.op;

        // Verificar si la location es válida
        if (assignStmt.location instanceof ArrayLocation) {
            checkArrayAccess((ArrayLocation) assignStmt.location);
        }

        if (exprType instanceof VoidType) {
            reportError("No se puede asignar una expresión de tipo void.");
            return;
        }

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

    private void checkArrayAccess(ArrayLocation arrayLoc) {
        Symbol symbol = symbolTable.lookup(arrayLoc.name);
        if (symbol == null) {
            reportError("El arreglo '" + arrayLoc.name + "' no está declarado.");
            return;
        }
        
        if (!(symbol.getType() instanceof ArrayType)) {
            reportError("La variable '" + arrayLoc.name + "' no es un arreglo.");
            return;
        }

        Type indexType = getExpressionType(arrayLoc.index);
        if (!(indexType instanceof IntType)) {
            reportError("El índice del arreglo debe ser de tipo int.");
        }

        // Si el índice es un literal, verificar que no sea negativo
        if (arrayLoc.index instanceof IntLiteral) {
            int index = ((IntLiteral) arrayLoc.index).getValue();
            if (index < 0) {
                reportError("El índice del arreglo no puede ser negativo.");
            }
        }
    }

    @Override
    public void visit(IfStmt ifStmt) {
        ifStmt.getCondition().accept(this);
        Type condType = getExpressionType(ifStmt.getCondition());
        
        if (!(condType instanceof BooleanType)) {
            reportError("La condición del 'if' debe ser de tipo boolean.");
        }

        boolean previousHasReturn = hasReturnStatement;
        
        // Analizar bloque then
        hasReturnStatement = false;
        ifStmt.getThenBlock().accept(this);
        boolean thenHasReturn = hasReturnStatement;

        // Analizar bloque else si existe
        boolean elseHasReturn = false;
        if (ifStmt.getElseBlock() != null) {
            hasReturnStatement = false;
            ifStmt.getElseBlock().accept(this);
            elseHasReturn = hasReturnStatement;
        }

        // Un if-else cuenta como retorno si ambas ramas retornan
        hasReturnStatement = (thenHasReturn && elseHasReturn) || previousHasReturn;
    }

    @Override
    public void visit(ForStmt forStmt) {
        // Verificar inicialización
        Statement init = forStmt.getInit();
        init.accept(this);
        
        // Verificar condición
        forStmt.getCondition().accept(this);
        Type condType = getExpressionType(forStmt.getCondition());
        if (!(condType instanceof BooleanType)) {
            reportError("La condición del for debe ser de tipo boolean");
        }

        // Verificar actualización
        forStmt.getUpdate().accept(this);
        
        // Incrementar el contador de loops anidados
        loopDepth++;  // Añade esta variable como campo de clase
        
        // Entrar en el loop
        boolean previousInLoop = inLoop;
        inLoop = true;
        
        // Visitar el cuerpo
        forStmt.getBody().accept(this);
        
        // Restaurar estado previo
        inLoop = previousInLoop;
        loopDepth--;  // Decrementar al salir
    }

   
    @Override
    public void visit(WhileStmt whileStmt) {
        whileStmt.getCondition().accept(this);
        Type condType = getExpressionType(whileStmt.getCondition());
        
        if (!(condType instanceof BooleanType)) {
            reportError("La condición del while debe ser de tipo boolean.");
        }

        loopDepth++;
        boolean previousInLoop = inLoop;
        inLoop = true;
        
        whileStmt.getBody().accept(this);
        
        inLoop = previousInLoop;
        loopDepth--;
    }

    @Override
    public void visit(ReturnStmt returnStmt) {
        if (currentMethodReturnType == null) {
            reportError("La sentencia return está fuera de un método.");
            return;
        }

        hasReturnStatement = true;

        if (returnStmt.getExpression() == null) {
            if (!(currentMethodReturnType instanceof VoidType)) {
                reportError("Se esperaba un valor de retorno en el método '" + currentMethodName + "'.");
            }
            return;
        }

        returnStmt.getExpression().accept(this);
        Type exprType = getExpressionType(returnStmt.getExpression());

        if (currentMethodReturnType instanceof VoidType) {
            reportError("El método void '" + currentMethodName + "' no debe retornar un valor.");
            return;
        }

        if (!typesAreCompatible(currentMethodReturnType, exprType)) {
            reportError("Tipo de retorno incorrecto en método '" + currentMethodName + 
                      "'. Se esperaba " + currentMethodReturnType + " pero se encontró " + exprType + ".");
        }
    }

    @Override
    public void visit(BreakStmt breakStmt) {
        if (loopDepth == 0) {
            reportError("La sentencia 'break' debe estar dentro de un ciclo.");
        }
    }

    @Override
    public void visit(ContinueStmt continueStmt) {
        if (loopDepth == 0) {
            reportError("La sentencia 'continue' debe estar dentro de un ciclo.");
        }
    }

    @Override
    public void visit(BinaryExpr binaryExpr) {
        binaryExpr.left.accept(this);
        binaryExpr.right.accept(this);

        Type leftType = getExpressionType(binaryExpr.left);
        Type rightType = getExpressionType(binaryExpr.right);
        String op = binaryExpr.op;

        // Verificar que ningún operando sea void
        if (leftType instanceof VoidType || rightType instanceof VoidType) {
            reportError("No se puede utilizar una expresión de tipo void en una operación binaria.");
            return;
        }

        // Verificar operaciones según el tipo de operador
        if (isArithmeticOp(op)) {
            checkArithmeticOperation(leftType, rightType, op);
        } else if (isRelationalOp(op)) {
            checkRelationalOperation(leftType, rightType, op);
        } else if (isEqualityOp(op)) {
            checkEqualityOperation(leftType, rightType, op);
        } else if (isConditionalOp(op)) {
            checkConditionalOperation(leftType, rightType, op);
        }
    }

    private void checkArithmeticOperation(Type leftType, Type rightType, String op) {
        if (!(leftType instanceof IntType) || !(rightType instanceof IntType)) {
            reportError("Operador aritmético '" + op + "' requiere operandos enteros.");
        }
    }

    private void checkRelationalOperation(Type leftType, Type rightType, String op) {
        if (!(leftType instanceof IntType) || !(rightType instanceof IntType)) {
            reportError("Operador relacional '" + op + "' requiere operandos enteros.");
        }
    }

    private void checkEqualityOperation(Type leftType, Type rightType, String op) {
        if (!typesAreCompatible(leftType, rightType)) {
            reportError("Operador de igualdad '" + op + "' requiere operandos del mismo tipo.");
        }
    }

    private void checkConditionalOperation(Type leftType, Type rightType, String op) {
        if (!(leftType instanceof BooleanType) || !(rightType instanceof BooleanType)) {
            reportError("Operador lógico '" + op + "' requiere operandos booleanos.");
        }
    }

    @Override
    public void visit(UnaryExpr unaryExpr) {
        unaryExpr.expr.accept(this);
        Type exprType = getExpressionType(unaryExpr.expr);
        String op = unaryExpr.op;

        if (exprType instanceof VoidType) {
            reportError("No se puede utilizar una expresión de tipo void en una operación unaria.");
            return;
        }

        if (op.equals("!")) {
            if (!(exprType instanceof BooleanType)) {
                reportError("El operador '!' requiere un operando booleano.");
            }
        } else if (op.equals("-")) {
            if (!(exprType instanceof IntType)) {
                reportError("El operador '-' unario requiere un operando entero.");
            }
        }
    }

    @Override
    public void visit(MethodCall methodCall) {
        // Verificar recursión infinita
        if (!currentCallStack.add(methodCall.getMethodName())) {
            reportError("Posible recursión infinita detectada en el método '" + methodCall.getMethodName() + "'.");
            currentCallStack.remove(methodCall.getMethodName());
            return;
        }

        Symbol methodSymbol = symbolTable.lookup(methodCall.getMethodName());
        if (methodSymbol == null || methodSymbol.getSymbolType() != Symbol.SymbolType.METHOD) {
            reportError("Método '" + methodCall.getMethodName() + "' no está declarado.");
            currentCallStack.remove(methodCall.getMethodName());
            return;
        }

        List<Expression> args = methodCall.getArguments();
        List<Type> paramTypes = methodSymbol.getParameterTypes();

        // Verificar número de argumentos
        if (args.size() != paramTypes.size()) {
            reportError("El método '" + methodCall.getMethodName() + "' espera " + 
                      paramTypes.size() + " argumentos, pero se proporcionaron " + args.size() + ".");
            currentCallStack.remove(methodCall.getMethodName());
            return;
        }

        // Verificar tipos de argumentos
        for (int i = 0; i < args.size(); i++) {
            Expression arg = args.get(i);
            arg.accept(this);
            Type expectedType = paramTypes.get(i);
            Type actualType = getExpressionType(arg);
            
            if (!typesAreCompatible(expectedType, actualType)) {
                reportError("Tipo de argumento " + (i + 1) + " en llamada a '" + 
                          methodCall.getMethodName() + "' no coincide. Se esperaba " + 
                          expectedType + " pero se obtuvo " + actualType + ".");
            }
        }

        currentCallStack.remove(methodCall.getMethodName());
    }

    @Override
    public void visit(CalloutCall calloutCall) {
        for (CalloutArg arg : calloutCall.getArgs()) {
            arg.accept(this);
        }
    }

    @Override
    public void visit(NewArrayExpr newArrayExpr) {
        newArrayExpr.getSize().accept(this);
        Type sizeType = getExpressionType(newArrayExpr.getSize());

        if (!(sizeType instanceof IntType)) {
            reportError("El tamaño del array debe ser de tipo int.");
            return;
        }

        if (newArrayExpr.getSize() instanceof IntLiteral) {
            int size = ((IntLiteral) newArrayExpr.getSize()).getValue();
            if (size <= 0) {
                reportError("El tamaño del array debe ser mayor que cero.");
            }
        }
    }

    @Override
    public void visit(VarLocation varLocation) {
        Symbol symbol = symbolTable.lookup(varLocation.name);
        if (symbol == null || symbol.getSymbolType() != Symbol.SymbolType.VARIABLE) {
            reportError("La variable '" + varLocation.name + "' no está declarada.");
        } else {
            System.out.println("Variable '" + varLocation.name + "' encontrada con tipo '" + 
                             symbol.getType() + "'.");
        }
    }

    @Override
    public void visit(ArrayLocation arrayLocation) {
        Symbol symbol = symbolTable.lookup(arrayLocation.name);
        if (symbol == null || symbol.getSymbolType() != Symbol.SymbolType.VARIABLE) {
            reportError("El arreglo '" + arrayLocation.name + "' no está declarado.");
            return;
        }

        if (!(symbol.getType() instanceof ArrayType)) {
            reportError("La variable '" + arrayLocation.name + "' no es un arreglo.");
            return;
        }

        arrayLocation.index.accept(this);
        Type indexType = getExpressionType(arrayLocation.index);
        if (!(indexType instanceof IntType)) {
            reportError("El índice del arreglo '" + arrayLocation.name + "' debe ser de tipo int.");
        }
    }

    // Métodos auxiliares
    private boolean isArithmeticOp(String op) {
        return op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/") || op.equals("%");
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

    private boolean typesAreCompatible(Type expected, Type actual) {
        if (expected == null || actual == null) {
            return false;
        }

        // Manejo especial para arrays
        if (expected instanceof ArrayType && actual instanceof ArrayType) {
            return typesAreCompatible(
                ((ArrayType) expected).getElementType(),
                ((ArrayType) actual).getElementType()
            );
        }

        // Comparación de tipos básicos
        return expected.getClass().equals(actual.getClass());
    }

    private Type getExpressionType(Expression expr) {
        if (expr instanceof IntLiteral) {
            return new IntType();
        } else if (expr instanceof BoolLiteral) {
            return new BooleanType();
        } else if (expr instanceof CharLiteral) {
            return new CharType();
        } else if (expr instanceof StringLiteral) {
            return new StringType();
        } else if (expr instanceof VarLocation) {
            Symbol symbol = symbolTable.lookup(((VarLocation) expr).name);
            if (symbol != null) {
                return symbol.getType();
            } else {
                reportError("La variable '" + ((VarLocation) expr).name + "' no está declarada.");
                return null;
            }
        } else if (expr instanceof ArrayLocation) {
            Symbol symbol = symbolTable.lookup(((ArrayLocation) expr).name);
            if (symbol != null && symbol.getType() instanceof ArrayType) {
                return ((ArrayType) symbol.getType()).getElementType();
            } else {
                reportError("El arreglo '" + ((ArrayLocation) expr).name + 
                          "' no está declarado o no es un arreglo.");
                return null;
            }
        } else if (expr instanceof BinaryExpr) {
            BinaryExpr binExpr = (BinaryExpr) expr;
            Type leftType = getExpressionType(binExpr.left);
            Type rightType = getExpressionType(binExpr.right);
            String op = binExpr.op;

            if (leftType instanceof VoidType || rightType instanceof VoidType) {
                reportError("No se puede utilizar una expresión de tipo void en una operación.");
                return null;
            }

            if (isArithmeticOp(op)) {
                return new IntType();
            } else if (isRelationalOp(op) || isEqualityOp(op) || isConditionalOp(op)) {
                return new BooleanType();
            }
        } else if (expr instanceof UnaryExpr) {
            UnaryExpr unExpr = (UnaryExpr) expr;
            Type exprType = getExpressionType(unExpr.expr);
            String op = unExpr.op;

            if (exprType instanceof VoidType) {
                reportError("No se puede utilizar una expresión de tipo void en una operación unaria.");
                return null;
            }

            if (op.equals("!")) {
                return new BooleanType();
            } else if (op.equals("-")) {
                return new IntType();
            }
        } else if (expr instanceof MethodCall) {
            MethodCall methodCall = (MethodCall) expr;
            Symbol methodSymbol = symbolTable.lookup(methodCall.getMethodName());
            if (methodSymbol != null) {
                if (methodSymbol.getType() instanceof VoidType) {
                    reportError("El método '" + methodCall.getMethodName() + 
                              "' no retorna un valor y no puede ser usado en una expresión.");
                    return new VoidType();
                }
                return methodSymbol.getType();
            } else {
                reportError("Método '" + methodCall.getMethodName() + "' no está declarado.");
                return null;
            }
        } else if (expr instanceof NewArrayExpr) {
            Type elementType = ((NewArrayExpr) expr).getElementType();
            return new ArrayType(elementType);
        }

        return null;
    }

    // Métodos de visita restantes (tipos básicos y literales)
    @Override
    public void visit(IntType intType) {
        // No requiere acción
    }

    @Override
    public void visit(BooleanType booleanType) {
        // No requiere acción
    }

    @Override
    public void visit(CharType charType) {
        // No requiere acción
    }

    @Override
    public void visit(VoidType voidType) {
        // No requiere acción
    }

    @Override
    public void visit(ArrayType arrayType) {
        // No requiere acción
    }

    @Override
    public void visit(StringType stringType) {
        // No requiere acción
    }

    @Override
    public void visit(NullType nullType) {
        // No requiere acción
    }

    @Override
    public void visit(IntLiteral intLiteral) {
        // No requiere acción
    }

    @Override
    public void visit(BoolLiteral boolLiteral) {
        // No requiere acción
    }

    @Override
    public void visit(CharLiteral charLiteral) {
        // No requiere acción
    }

    @Override
    public void visit(StringLiteral stringLiteral) {
        // No requiere acción
    }

    @Override
    public void visit(MultiVarDecl multiVarDecl) {
        System.out.println("visit(MultiVarDecl): Procesando declaraciones múltiples de variables.");
        for (ClassBodyMember decl : multiVarDecl.getDeclarations()) {
            if (decl instanceof VarDecl) {
                decl.accept(this);
            } else {
                System.out.println("Advertencia: Encontrado ClassBodyMember que no es VarDecl en MultiVarDecl.");
            }
        }
    }

    @Override
    public void visit(Param param) {
        // Parámetros son manejados en la visita del método
    }

    @Override
    public void visit(MethodCallStmt methodCallStmt) {
        methodCallStmt.getMethodCall().accept(this);
    }

    @Override
    public void visit(ExprStmt exprStmt) {
        exprStmt.getExpression().accept(this);
    }

    @Override
    public void visit(StringArg stringArg) {
        // No requiere acción específica
    }

    @Override
    public void visit(ExprArg exprArg) {
        exprArg.getExpression().accept(this);
    }

    @Override
    public void visit(VarDeclStmt varDeclStmt) {
        varDeclStmt.getVarDecl().accept(this);
        if (varDeclStmt.getInitExpression() != null) {
            varDeclStmt.getInitExpression().accept(this);
            Type varType = varDeclStmt.getVarDecl().type;
            Type exprType = getExpressionType(varDeclStmt.getInitExpression());
            if (!typesAreCompatible(varType, exprType)) {
                reportError("Tipo de la expresión de inicialización no coincide con el tipo declarado.");
            }
        }
    }

    @Override
    public void visit(CalloutStmt calloutStmt) {
        CalloutCall call = calloutStmt.getCalloutCall();
        // En Decaf, los callouts no requieren declaración previa
        // Visitar los argumentos
        for (CalloutArg arg : call.getArgs()) {
            arg.accept(this);
        }
    }

    @Override
    public void visit(AssignExpr assignExpr) {
        assignExpr.getLocation().accept(this);
        assignExpr.getExpression().accept(this);

        Type locType = getExpressionType(assignExpr.getLocation());
        Type exprType = getExpressionType(assignExpr.getExpression());
        String op = assignExpr.getOperator();

        if (!typesAreCompatible(locType, exprType)) {
            reportError("Tipos incompatibles en asignación.");
        }

        if ((op.equals("+=") || op.equals("-=")) && 
            (!(locType instanceof IntType) || !(exprType instanceof IntType))) {
            reportError("Los operadores += y -= solo pueden usarse con tipos enteros.");
        }
    }
}