package seedu.exceptions;

public class InventraOutOfBoundsException extends InventraException {
    private final int start;
    private final int end;
    private final int index;

    public InventraOutOfBoundsException(int index, int start, int end) {
        this.index = index;
        this.start = start;
        this.end = end;
    }

    @Override
    public String getMessage() {
        return String.format("Index %d is outside the bounds %d to %d", index, start, end);
    }
}
