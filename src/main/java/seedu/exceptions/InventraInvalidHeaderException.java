package seedu.exceptions;

public class InventraInvalidHeaderException extends InventraException {
    private final String headerName;

    public InventraInvalidHeaderException(String headerName) {
        this.headerName = headerName;
    }

    public String getMessage() {
        return "Error: " + headerName + " does not exist";
    }
}
