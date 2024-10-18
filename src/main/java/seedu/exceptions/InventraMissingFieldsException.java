package seedu.exceptions;

public class InventraMissingFieldsException extends InventraException {
    @Override
    public String getMessage() {
        return "Error: Inventory is missing fields\nUse 'add -h <s/pname, i/quantity, ...>' to add fields.";
    }
}
