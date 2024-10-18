package seedu.exceptions;

public class InventraInvalidCommandException extends InventraException {
    private final String command;

    public InventraInvalidCommandException(String command) {
        this.command = command;
    }

    @Override
    public String getMessage() {
        return "Error: The command '" + command + "' is invalid. Type help to receive valid commands";
    }
}

