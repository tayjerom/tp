package seedu.exceptions;

public class InventraInvalidFlagException extends InventraException {
    private final String message;

    public InventraInvalidFlagException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return "Error: Invalid flags used\n" + message;
    }
}
