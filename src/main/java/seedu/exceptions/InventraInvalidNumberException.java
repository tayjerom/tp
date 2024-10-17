package seedu.exceptions;

public class InventraInvalidNumberException extends InventraException {
    private final String invalidNumber;

    public InventraInvalidNumberException(String input) {
        invalidNumber = input;
    }

    @Override
    public String getMessage() {
        return String.format("Error: The input %s could not be parsed as an integer.", invalidNumber);
    }
}
