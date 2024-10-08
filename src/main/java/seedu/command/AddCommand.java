package seedu.command;

import seedu.model.Inventory;
import seedu.ui.Ui;

import java.util.HashMap;
import java.util.Map;

public class AddCommand {
    private Inventory inventory;
    private Ui ui;

    public AddCommand(Inventory inventory, Ui ui) {
        this.inventory = inventory;
        this.ui = ui;
    }

    public void execute(String[] args) {
        if (args.length < 2) {
            ui.showErrorInvalidCommand();
            return;
        }

        String flag = args[1];

        switch (flag) {
        case "-h":
            if (args.length < 3) {
                ui.showErrorNoFields();
                return;
            }
            handleAddMultipleFields(args[2]);
            break;

        case "-l":
            ui.showFieldsAndRecords(inventory);
            break;

        case "-d":
            if (args.length < 3) {
                ui.showErrorNoRecords();
                return;
            }
            handleAddRecord(args[2]);
            break;

        default:
            ui.showErrorInvalidFlag();
            break;
        }
    }

    private void handleAddMultipleFields(String fieldData) {
        if (fieldData.isEmpty()) {
            ui.showErrorNoFields();
            return;
        }

        String[] newFields = fieldData.split(",\\s*");
        for (String field : newFields) {
            String[] parts = field.split("/");
            if (parts.length != 2) {
                ui.showErrorInvalidFieldFormat();
                return;
            }

            String type = parts[0].trim();
            String fieldName = parts[1].trim();
            inventory.addField(fieldName, type);
        }

        ui.showSuccessFieldsAdded();
        ui.showFieldsAndRecords(inventory);  // Show fields after adding
    }

    private void handleAddRecord(String recordData) {
        if (inventory.getFields().isEmpty()) {
            ui.showErrorNoFieldsDefined();
            return;
        }

        String[] values = recordData.split(",\\s*");
        if (values.length != inventory.getFields().size()) {
            ui.showErrorInvalidRecordCount(inventory.getFields().size());
            return;
        }

        Map<String, String> record = new HashMap<>();
        for (int i = 0; i < values.length; i++) {
            String field = inventory.getFields().get(i);
            String type = inventory.getFieldTypes().get(field);
            String value = values[i].trim();

            // Validate based on field type
            String validationMessage = validateValue(value, type, field);
            if (validationMessage != null) {
                ui.showValidationError(validationMessage);
                return;
            }

            record.put(field, value);
        }

        inventory.addRecord(record);
        ui.showSuccessRecordAdded();
    }

    private String validateValue(String value, String type, String field) {
        switch (type) {
        case "s": // String
            return null; // Any string is valid
        case "i": // Integer
            try {
                Integer.parseInt(value);
                return null; // Valid integer
            } catch (NumberFormatException e) {
                return ui.getInvalidIntegerMessage(field, value);
            }
        case "f": // Float
            try {
                Float.parseFloat(value);
                return null; // Valid float
            } catch (NumberFormatException e) {
                return ui.getInvalidFloatMessage(field, value);
            }
        case "d": // Date
            // Simple date validation, assuming the format is "dd/MM/yyyy"
            String[] parts = value.split("/");
            if (parts.length != 3) {
                return ui.getInvalidDateMessage(field, value);
            }
            try {
                int day = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int year = Integer.parseInt(parts[2]);
                if (day <= 0 || month <= 0 || month > 12) {
                    return ui.getInvalidDateMessage(field, value);
                }
                return null; // Valid date
            } catch (NumberFormatException e) {
                return ui.getInvalidDateMessage(field, value);
            }
        case "n": // Null
            if (!value.equalsIgnoreCase("null")) {
                return ui.getInvalidNullMessage(field, value);
            }
            return null; // Valid null
        default:
            return ui.getUnknownTypeMessage(field);
        }
    }
}
