package aoc22;

public class ShuffleOperation {

    private Operation operation;
    private int parameter;

    public ShuffleOperation(Operation operation) {
        this.operation = operation;
    }

    public ShuffleOperation(Operation operation, int parameter) {
        this.operation = operation;
        this.parameter = parameter;
    }

    public Operation getOperation() {
        return operation;
    }

    public int getParameter() {
        return parameter;
    }

    public enum Operation {
        NEW_STACK, CUT, INCREMENT;
    }
}
