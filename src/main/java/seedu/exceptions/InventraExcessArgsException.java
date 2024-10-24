package seedu.exceptions;

public class InventraExcessArgsException extends InventraException {
    private final int expected;
    private final int actual;

    public InventraExcessArgsException(int expected, int actual) {
        this.expected = expected;
        this.actual = actual;
    }

    public String getMessage() {
        return "Error: More arguments than required, expected: " + expected + ", received " + actual;
    }
}
