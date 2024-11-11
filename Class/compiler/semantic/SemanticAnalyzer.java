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
        System.out.println("Declarando variables globales...");
        for (ClassBodyMember member : program.classBody) {
            System.out.println("Miembro de clase: " + member.getClass().getSimpleName());
            if (member instanceof VarDecl) {
                System.out.println("Es una VarDecl.");
                member.accept(this); // Agregar variables globales al scope global
            } else if (member instanceof MultiVarDecl) {
                System.out.println("Es una MultiVarDecl.");
                member.accept(this); // Procesar MultiVarDecl
            } else if (member instanceof MethodDecl) {
                System.out.println("Es una MethodDecl.");
                // No hacemos nada aquí, se maneja más adelante
            } else {
                System.out.println("Tipo de miembro de clase no reconocido.");
            }
        }

        // Después de declarar variables globales
        System.out.println("Contenido de la tabla de símbolos después de declarar variables globales:");
        symbolTable.printAllScopes();

        // Declarar métodos
        System.out.println("Declarando métodos...");
        for (ClassBodyMember member : program.classBody) {
            if (member instanceof MethodDecl) {
                MethodDecl methodDecl = (MethodDecl) member;
                String methodName = methodDecl.name;
                Type returnType = methodDecl.returnType;

                // Crear símbolo del método con el tipo METHOD
                Symbol methodSymbol = new Symbol(methodName, returnType, Symbol.SymbolType.METHOD);
                for (Param param : methodDecl.params) {
                    methodSymbol.addParameterType(param.type);
                }

                if (!symbolTable.declare(methodSymbol)) {
                    reportError("Método '" + methodName + "' ya está declarado.");
                }
            }
        }

        // Visitar los métodos para analizarlos
        System.out.println("Analizando métodos...");
        for (ClassBodyMember member : program.classBody) {
            if (member instanceof MethodDecl) {
                member.accept(this);
            }
        }

        // Verificar que exista el método main
        Symbol mainMethod = symbolTable.lookup("main");
        if (mainMethod == null || mainMethod.getSymbolType() != Symbol.SymbolType.METHOD) {
            reportError("El programa debe contener un método 'main'.");
        } else if (!mainMethod.getParameterTypes().isEmpty()) {
            reportError("El método 'main' no debe tener parámetros.");
        }
    }

    @Override
    public void visit(VarDecl varDecl) {
        Type type = varDecl.type;
        String name = varDecl.name;

        System.out.println("visit(VarDecl): Declarando variable '" + name + "' de tipo '" + type + "'.");

        // Crear símbolo de la variable con el tipo VARIABLE
        Symbol symbol = new Symbol(name, type, Symbol.SymbolType.VARIABLE);

        if (!symbolTable.declare(symbol)) {
            reportError("Identificador '" + name + "' ya está declarado en este scope.");
        } else {
            System.out.println("Variable '" + name + "' declarada en el scope actual.");
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

        // Ya declaramos los métodos en el visit(Program), no es necesario hacerlo aquí

        // Manejar el scope del método
        symbolTable.enterScope();
        System.out.println("Entrando al scope del método '" + methodName + "'.");

        // Insertar parámetros en el scope del método
        for (Param param : methodDecl.params) {
            Symbol paramSymbol = new Symbol(param.name, param.type, Symbol.SymbolType.VARIABLE);
            if (!symbolTable.declare(paramSymbol)) {
                reportError("Parámetro '" + param.name + "' ya está declarado en este método.");
            } else {
                System.out.println("Parámetro '" + param.name + "' declarado en el scope del método.");
            }
        }

        // Manejar el retorno
        Type previousReturnType = currentMethodReturnType;
        currentMethodReturnType = returnType;

        // Verificar si es el método main
        boolean previousInMainMethod = inMainMethod;
        if (methodName.equals("main")) {
            inMainMethod = true;
        }

        // Visitar el cuerpo del método
        methodDecl.body.accept(this);

        // Restaurar el estado anterior
        currentMethodReturnType = previousReturnType;
        inMainMethod = previousInMainMethod;

        // Salir del scope del método
        symbolTable.exitScope();
        System.out.println("Saliendo del scope del método '" + methodName + "'.");
    }

    @Override
    public void visit(StringType stringType) {
        // No se necesita acción específica
    }

    @Override
    public void visit(Block block) {
        // Manejar el scope del bloque
        symbolTable.enterScope();
        System.out.println("Entrando a un nuevo scope de bloque.");

        // Declaraciones de variables
        for (VarDecl varDecl : block.varDecls) {
            varDecl.accept(this);
        }

        // Sentencias
        for (Statement stmt : block.statements) {
            stmt.accept(this);
        }

        symbolTable.exitScope();
        System.out.println("Saliendo del scope de bloque.");
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
        assignStmt.location.accept(this);
        assignStmt.expr.accept(this);

        Type locType = getExpressionType(assignStmt.location);
        Type exprType = getExpressionType(assignStmt.expr);
        String op = assignStmt.op;

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

    @Override
    public void visit(IfStmt ifStmt) {
        ifStmt.getCondition().accept(this);
        Type condType = getExpressionType(ifStmt.getCondition());
        if (condType != null && !(condType instanceof BooleanType)) {
            reportError("La condición del 'if' debe ser de tipo boolean.");
        }
        ifStmt.getThenBlock().accept(this);
        if (ifStmt.getElseBlock() != null) {
            ifStmt.getElseBlock().accept(this);
        }
    }

    @Override
    public void visit(MultiVarDecl multiVarDecl) {
        System.out.println("visit(MultiVarDecl): Procesando declaraciones múltiples de variables.");
        // Itera sobre cada declaración y verifica su tipo
        for (ClassBodyMember decl : multiVarDecl.getDeclarations()) {
            if (decl instanceof VarDecl) {
                decl.accept(this);
            } else {
                System.out.println("Advertencia: Encontrado ClassBodyMember que no es VarDecl en MultiVarDecl.");
            }
        }
    }


    @Override
    public void visit(ForStmt forStmt) {
        forStmt.getInit().accept(this);
        forStmt.getCondition().accept(this);
        Type condType = getExpressionType(forStmt.getCondition());
        // La condición del for debe ser booleana
        if (condType != null && !(condType instanceof BooleanType)) {
            reportError("La condición del 'for' debe ser de tipo boolean.");
        }
        forStmt.getUpdate().accept(this);

        boolean previousInLoop = inLoop;
        inLoop = true;
        forStmt.getBody().accept(this);
        inLoop = previousInLoop;
    }

    @Override
    public void visit(WhileStmt whileStmt) {
        whileStmt.getCondition().accept(this);
        // Verificar que la condición sea booleana
        Type condType = getExpressionType(whileStmt.getCondition());
        if (!(condType instanceof BooleanType)) {
            reportError("La condición del 'while' debe ser de tipo boolean.");
        }
        // Visitar el cuerpo
        boolean previousInLoop = inLoop;
        inLoop = true;
        whileStmt.getBody().accept(this);
        inLoop = previousInLoop;
    }

    @Override
    public void visit(ExprArg exprArg) {
        // Verificar el tipo de la expresión
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
            Type exprType = getExpressionType(returnStmt.getExpression());

            if (exprType == null) {
                reportError("No se pudo determinar el tipo de la expresión de retorno.");
                return;
            }

            if (exprType instanceof VoidType) {
                reportError("No se puede retornar una expresión de tipo void.");
                return;
            }

            if (!typesAreCompatible(currentMethodReturnType, exprType)) {
                reportError("La expresión de retorno debe ser de tipo " + currentMethodReturnType + ", pero es de tipo " + exprType + ".");
            }
            if (currentMethodReturnType instanceof VoidType && returnStmt.getExpression() != null) {
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
        CalloutCall call = calloutStmt.getCalloutCall();
        // En Decaf, los callouts no requieren declaración previa
        // Visitar los argumentos
        for (CalloutArg arg : call.getArgs()) {
            arg.accept(this);
        }
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

    @Override
    public void visit(BinaryExpr binaryExpr) {
        binaryExpr.left.accept(this);
        binaryExpr.right.accept(this);

        Type leftType = getExpressionType(binaryExpr.left);
        Type rightType = getExpressionType(binaryExpr.right);
        String op = binaryExpr.op;

        if (leftType instanceof VoidType || rightType instanceof VoidType) {
            reportError("No se puede utilizar una expresión de tipo void en una operación.");
            return;
        }

        if (isArithmeticOp(op)) {
            if (!(leftType instanceof IntType) || !(rightType instanceof IntType)) {
                reportError("Operador aritmético requiere operandos enteros.");
            }
        } else if (isRelationalOp(op)) {
            if (!(leftType instanceof IntType) || !(rightType instanceof IntType)) {
                reportError("Operador relacional requiere operandos enteros.");
            }
        } else if (isEqualityOp(op)) {
            if (!typesAreCompatible(leftType, rightType)) {
                reportError("Operador de igualdad requiere operandos del mismo tipo.");
            }
        } else if (isConditionalOp(op)) {
            if (!(leftType instanceof BooleanType) || !(rightType instanceof BooleanType)) {
                reportError("Operador lógico requiere operandos booleanos.");
            }
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
                reportError("El operando del operador '!' debe ser de tipo boolean.");
            }
        } else if (op.equals("-")) {
            if (!(exprType instanceof IntType)) {
                reportError("El operando del operador '-' unario debe ser de tipo int.");
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
            arg.accept(this);
            Type expectedType = paramTypes.get(i);
            Type actualType = getExpressionType(arg);
            if (actualType == null || !typesAreCompatible(expectedType, actualType)) {
                reportError("Tipo de argumento " + (i + 1) + " en llamada a '" + methodCall.getMethodName() + "' no coincide. Se esperaba " + expectedType + " pero se obtuvo " + actualType + ".");
            }
        }

        // No es necesario verificar aquí si el método retorna void y se usa como expresión,
        // ya que esto se maneja en getExpressionType
    }

    @Override
    public void visit(CalloutCall calloutCall) {
        // En DECAF, los callouts no requieren declaración previa
        // Visitar los argumentos
        for (CalloutArg arg : calloutCall.getArgs()) {
            arg.accept(this);
        }
    }

    @Override
    public void visit(NewArrayExpr newArrayExpr) {
        // Visitar la expresión del tamaño
        newArrayExpr.getSize().accept(this);
        Type sizeType = getExpressionType(newArrayExpr.getSize());

        // Verificar que el tamaño es de tipo int
        if (!(sizeType instanceof IntType)) {
            reportError("El tamaño del array debe ser de tipo int.");
        }

        // Verificar que el tamaño es mayor que cero si es un literal
        if (newArrayExpr.getSize() instanceof IntLiteral) {
            int sizeValue = ((IntLiteral) newArrayExpr.getSize()).getValue();
            if (sizeValue <= 0) {
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
            System.out.println("Variable '" + varLocation.name + "' encontrada con tipo '" + symbol.getType() + "'.");
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
        } else {
            System.out.println("Arreglo '" + arrayLocation.name + "' encontrado con tipo '" + symbol.getType() + "'.");
        }

        arrayLocation.index.accept(this);
        // Verificar que el índice sea de tipo int
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
        // No se necesita acción específica
    }

    @Override
    public void visit(CharLiteral charLiteral) {
        // No se necesita acción
    }

    @Override
    public void visit(StringLiteral stringLiteral) {
        // No se necesita acción
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

    private boolean typesAreCompatible(Type expected, Type actual) {
        if (expected == null || actual == null) {
            return false;
        }

        // Si ambos son arreglos, compara sus tipos base
        if (expected instanceof ArrayType && actual instanceof ArrayType) {
            return typesAreCompatible(
                ((ArrayType) expected).getElementType(),
                ((ArrayType) actual).getElementType()
            );
        }

        // Comparar tipos básicos
        return expected.getClass().equals(actual.getClass());
    }

    @Override
    public void visit(NullType nullType) {
        // No se necesita hacer nada específico aquí
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
                reportError("El arreglo '" + ((ArrayLocation) expr).name + "' no está declarado o no es un arreglo.");
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
            } else if (isRelationalOp(op)) {
                return new BooleanType();
            } else if (isEqualityOp(op)) {
                return new BooleanType();
            } else if (isConditionalOp(op)) {
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
            // Manejar otros operadores unarios si existen
        } else if (expr instanceof MethodCall) {
            MethodCall methodCall = (MethodCall) expr;
            Symbol methodSymbol = symbolTable.lookup(methodCall.getMethodName());
            if (methodSymbol != null) {
                if (methodSymbol.getType() instanceof VoidType) {
                    reportError("El método '" + methodCall.getMethodName() + "' no retorna un valor y no puede ser usado en una expresión.");
                    return new VoidType();
                }
                return methodSymbol.getType();
            } else {
                reportError("Método '" + methodCall.getMethodName() + "' no está declarado.");
                return null;
            }
        } else if (expr instanceof NewArrayExpr) {
            // Retorna el tipo del arreglo
            Type elementType = ((NewArrayExpr) expr).getElementType();
            return new ArrayType(elementType);
        }

        // Manejar otros tipos de expresiones según tu AST
        return null; // Por defecto
    }
}
