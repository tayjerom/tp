package seedu.exceptions;

public class InventraNegativeValueException extends InventraException {
    private final String field;

    public InventraNegativeValueException(String field, String value) {
        super();
        this.field = field;
    }

    @Override
    public String getMessage() {
        return "Error: Negative value detected in field '" + field + "'. Value must be non-negative.";
    }
}
