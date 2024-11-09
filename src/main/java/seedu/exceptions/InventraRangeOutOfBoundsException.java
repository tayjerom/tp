package seedu.exceptions;

public class InventraRangeOutOfBoundsException extends InventraException {
    private final int start;
    private final int end;
    private final int startIndex;
    private final int endIndex;

    public InventraRangeOutOfBoundsException(int startIndex, int endIndex, int start, int end) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.start = start;
        this.end = end;
    }

    @Override
    public String getMessage() {
        if (end == 0) {
            return "Error: The inventory is empty.\nUse 'add -d <value1, value2, ...>' to add records.";
        }
        if (endIndex < startIndex) {
            return String.format("Error: End index value: %d cannot be smaller than start index value: %d."
                    , startIndex, endIndex);
        }
        return String.format("Error: Range %d to %d is outside the bounds %d to %d", startIndex, endIndex, start, end);
    }
}
