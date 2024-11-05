package seedu.exceptions;

public class InventraInvalidRecordCountException extends InventraException {
    private final int expectedCount;
    private final int actualCount;

    public InventraInvalidRecordCountException(int expectedCount, int actualCount) {
        this.expectedCount = expectedCount;
        this.actualCount = actualCount;
    }

    @Override
    public String getMessage() {
        return "Error: Invalid number of values.\n" +
                "Expected " + expectedCount + " values, but got " + actualCount + ".";
    }
}
