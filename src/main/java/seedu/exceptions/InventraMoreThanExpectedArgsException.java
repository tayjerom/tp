package seedu.exceptions;

public class InventraMoreThanExpectedArgsException extends InventraException {
    private int expected;
    private int actual;

    public InventraMoreThanExpectedArgsException(int expected, int actual) {
        this.expected = expected;
        this.actual = actual;
    }

    public String getMessage() {
        return "Error: More argument that required, expect " + expected + " inputs, receive " + actual + " inputs.";
    }
}
