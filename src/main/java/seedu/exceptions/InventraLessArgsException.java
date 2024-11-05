package seedu.exceptions;

public class InventraLessArgsException extends InventraException {
    private final int expected;
    private final int actual;

    public InventraLessArgsException(int expected, int actual) {
        this.expected = expected;
        this.actual = actual;
    }

    public String getMessage() {
        return "Error: Less arguments than required, expected: " + expected + ", received " + actual;
    }
}
