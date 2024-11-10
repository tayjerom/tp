package seedu.exceptions;

public class InventraLessArgsException extends InventraException {
    private final int expected;
    private final int actual;

    public InventraLessArgsException(int expected, int actual) {
        this.expected = expected;
        this.actual = actual;
    }

    @Override
    public String getMessage() {
        return String.format("Error: Less arguments than required, expected: %d, received: %d", expected, actual);
    }
}

