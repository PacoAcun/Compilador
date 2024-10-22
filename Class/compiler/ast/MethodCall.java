package compiler.ast;

import java.util.List;
import java.util.ArrayList;

public class MethodCall extends Expression {
    protected String methodName;
    protected List<Expression> arguments;

    public MethodCall(String methodName, List<Expression> arguments) {
        this.methodName = methodName;
        this.arguments = arguments;
    }

    public String getMethodName() {
        return methodName;
    }

    public List<Expression> getArguments() {
        return arguments;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
