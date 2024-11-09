package seedu.exceptions;

public class InventraInvalidTypeException extends InventraException {
    private final String customMessage;

    public InventraInvalidTypeException(String field, String input, String expectedType) {
        this.customMessage = String.format(
                "Error: Invalid type for field '%s'%nExpected value of type '%s', got: '%s'",
                field, expectedType, input
        );
    }

    public InventraInvalidTypeException(String customMessage) {
        this.customMessage = customMessage;
    }

    @Override
    public String getMessage() {
        return customMessage;
    }
}
