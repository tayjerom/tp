package seedu.exceptions;

public class InventraException extends RuntimeException {
    @Override
    public String getMessage() {
        return "An error has occurred";
    }
}
