package ast;

import java.util.List;

public class Program {
    private DeclList declList;

    public Program(DeclList declList) {
        this.declList = declList;
    }

    public DeclList getDeclList() {
        return declList;
    }

    @Override
    public String toString() {
        return "Program{" + "declList=" + declList + '}';
    }
}