package seedu.command;

import seedu.model.Inventory;
import seedu.storage.Csv;
import seedu.ui.Ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class AddCommand {
    private Inventory inventory;
    private Ui ui;
    private Csv csv;

    public AddCommand(Inventory inventory, Ui ui, Csv csv) {
        this.inventory = inventory;
        this.ui = ui;
        this.csv = csv;
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
            csv.updateCsvHeaders(inventory);
            break;

        case "-hu":
            if (args.length < 3) {
                ui.showErrorNoFields();
                return;
            }
            handleUpdateFields(args[2]);
            csv.updateCsvHeaders(inventory);
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
            csv.appendRecord(inventory.getRecords().get(inventory.getRecords().size() - 1), inventory);
            break;

        default:
            ui.showErrorInvalidFlag();
            break;
        }
    }

    private void handleUpdateFields(String fieldData) {
        if (fieldData.isEmpty()) {
            ui.showErrorNoFields();
            return;
        }

        String[] fieldsToUpdate = fieldData.split(",\\s*");
        List<String> updatedFields = new ArrayList<>();
        Map<String, String> updatedFieldTypes = new HashMap<>();

        // Process each field in the -hu command
        for (String field : fieldsToUpdate) {
            String[] parts = field.split("/");
            if (parts.length != 2) {
                ui.showErrorInvalidFieldFormat();
                return;
            }

            String type = parts[0].trim();
            String fieldName = parts[1].trim();

            if (inventory.getFields().contains(fieldName)) {
                // Update existing field
                ui.printMessage("    Field '" + fieldName + "' updated successfully with new type '" + type + "'.");
            } else {
                // Add new field if it doesn't exist
                ui.printMessage("    Field '" + fieldName + "' added successfully.");
            }
            // Add or update field in the new list of fields
            updatedFields.add(fieldName);
            updatedFieldTypes.put(fieldName, type);
        }

        // Replace inventory fields and types with the updated ones
        inventory.setFields(updatedFields);
        inventory.setFieldTypes(updatedFieldTypes);

        // Update the CSV headers to match the new field list
        csv.updateCsvHeaders(inventory);

        // Display the updated fields
        ui.showFieldsAndRecords(inventory);
    }

    private void handleAddMultipleFields(String fieldData) {
        if (fieldData.isEmpty()) {
            ui.showErrorNoFields();
            return;
        }

        String[] newFields = fieldData.split(",\\s*");
        boolean success = true;

        for (String field : newFields) {
            String[] parts = field.split("/");
            if (parts.length != 2) {
                ui.showErrorInvalidFieldFormat();
                success = false;
                continue;
            }

            String type = parts[0].trim();
            String fieldName = parts[1].trim();

            // Check for valid type
            if (!isValidFieldType(type)) {
                ui.showUnknownTypeMessage(type);
                success = false;
                continue;
            }

            // Check for duplicate fields
            if (inventory.getFields().contains(fieldName)) {
                ui.printMessage("Field '" + fieldName + "' already exists. Cannot add duplicate headers.");
                success = false;
                continue;
            }

            // Add field if all checks pass
            inventory.addField(fieldName, type);
        }

        if (success) {
            ui.showSuccessFieldsAdded();
        } else {
            ui.printMessage("Failed to add one or more fields due to errors.");
        }

        ui.showFieldsAndRecords(inventory);  // Show fields after attempting to add
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

    private boolean isValidFieldType(String type) {
        return type.equals("s") || type.equals("i") || type.equals("f") || type.equals("d");
    }
}
