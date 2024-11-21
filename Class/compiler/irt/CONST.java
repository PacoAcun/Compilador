package compiler.irt;

public class CONST extends IRExp {
    public int value;

    public CONST(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "CONST " + value;
    }
}
