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
        if (end == 0) {
            return "Error: The inventory is empty.\nUse 'add -d <value1, value2, ...>' to add records.";
        }
        return String.format("Error: Index %d is outside the bounds %d to %d", index, start, end);
    }
}
