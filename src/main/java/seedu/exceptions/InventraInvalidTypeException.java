package seedu.exceptions;

public class InventraInvalidTypeException extends InventraException {
    private final String field;
    private final String input;
    private final String expectedType;

    public InventraInvalidTypeException(String field, String input, String expectedType) {
        this.field = field;
        this.input = input;
        this.expectedType = expectedType;
    }

    @Override
    public String getMessage() {
        return "Error: Invalid type for field '" + field + "'\n" +
                "Expected value of type '" + expectedType + "', got: '" + input + "'";
    }
}
