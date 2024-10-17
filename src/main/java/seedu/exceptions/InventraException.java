package seedu.exceptions;

public class InventraException extends Exception {
    @Override
    public String getMessage() {
        return "Error: An error has occurred";
    }
}
