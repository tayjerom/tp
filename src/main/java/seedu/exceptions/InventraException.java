package seedu.exceptions;

public class InventraException extends Exception {
    public InventraException(String message) {
        super(message);
    }

    public InventraException() {
        super("Error: An error has occurred");
    }
}

