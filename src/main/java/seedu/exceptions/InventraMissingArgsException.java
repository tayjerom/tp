package seedu.exceptions;

public class InventraMissingArgsException extends InventraException {
    private final String[] args;

    public InventraMissingArgsException(String... args) {
        this.args = args;
    }

    @Override
    public String getMessage() {
        return "Error: Missing the following arguments: " + String.join(", ", args);
    }
}
