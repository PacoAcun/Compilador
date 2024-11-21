package compiler.irt;

public class TEMP extends IRExp {
    public String name;

    public TEMP(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TEMP " + name;
    }
}
