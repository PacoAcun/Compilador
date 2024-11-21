package compiler.irt;

import java.util.List;

public class SEQ extends IRStmt {
    public List<IRStmt> stmts;

    public SEQ(List<IRStmt> stmts) {
        this.stmts = stmts;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (IRStmt stmt : stmts) {
            sb.append(stmt.toString()).append("\n");
        }
        return sb.toString();
    }
}
