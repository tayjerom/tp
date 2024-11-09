package seedu.exceptions;

public class InventraOutOfBoundsException extends InventraException {
    private final int provided;
    private final int min;
    private final int max;

    public InventraOutOfBoundsException(int provided, int min, int max) {
        this.provided = provided;
        this.min = min;
        this.max = max;
    }

    @Override
    public String getMessage() {
        return String.format("Error: ID %d is out of bounds. Valid range is between %d and %d.", provided, min, max);
    }
}
