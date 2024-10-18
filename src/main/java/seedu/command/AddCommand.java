package seedu.command;

import seedu.exceptions.*;
import seedu.model.Inventory;
import seedu.storage.Csv;
import seedu.ui.Ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.logging.Logger;

public class AddCommand {
    private static final Logger LOGGER = Logger.getLogger(AddCommand.class.getName());

    private final Inventory inventory;
    private final Ui ui;
    private final Csv csv;

    public AddCommand(Inventory inventory, Ui ui, Csv csv) {
        assert inventory != null : "Inventory should not be null";
        assert ui != null : "Ui should not be null";
        assert csv != null : "Csv should not be null";

        this.inventory = inventory;
        this.ui = ui;
        this.csv = csv;
    }

    public void execute(String[] args) throws InventraException {
        assert args != null && args.length >= 2 : "Arguments should " +
                "contain at least a command and a flag";

        String flag = args[1];

        switch (flag) {
        case "-h":
            assert args.length >= 3 : "Expected additional field data for flag -h";
            handleAddMultipleFields(args[2]);
            csv.updateCsvHeaders(inventory);
            break;

        case "-hu":
            assert args.length >= 3 : "Expected additional field data for flag -hu";
            handleUpdateFields(args[2]);
            csv.updateCsvHeaders(inventory);
            break;

        case "-l":
            ui.showFieldsAndRecords(inventory);
            break;

        case "-d":
            assert args.length >= 3 : "Expected record data for flag -d";
            handleAddRecord(args[2]);
            csv.appendRecord(inventory.getRecords().get(inventory.getRecords().size() - 1), inventory);
            break;

        default:
            throw new InventraInvalidFlagException("Use 'add -h <fields>' 'add -l', or 'add -d <values>'");
        }
    }

    private void handleUpdateFields(String fieldData) throws InventraException {
        if (fieldData.isEmpty()) {
            throw new InventraMissingFieldsException();
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

    private void handleAddMultipleFields(String fieldData) throws InventraException {
        if (fieldData.isEmpty()) {
            throw new InventraMissingFieldsException();
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

    private void handleAddRecord(String recordData) throws InventraException {
        LOGGER.info("Handling add record: " + recordData);

        if (inventory.getFields().isEmpty()) {
            throw new InventraMissingFieldsException();
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

            assert type != null : "Type for field '" + field + "' should not be null.";

            if (type == null) {
                LOGGER.severe("Type for field '" + field + "' is null.");
                ui.printMessage("Error: Field '" + field + "' has no type defined.");
                return;
            }

            String value = values[i].trim();
            LOGGER.info("Processing field: " + field + ", Type: " + type + ", Value: " + value);

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

    private String validateValue(String value, String type, String field) throws InventraException {
        switch (type) {
        case "s": // String
            return null; // Any string is valid
        case "i": // Integer
            try {
                Integer.parseInt(value);
                return null; // Valid integer
            } catch (NumberFormatException e) {
                throw new InventraInvalidTypeException(field, value, type);
            }
        case "f": // Float
            try {
                Float.parseFloat(value);
                return null; // Valid float
            } catch (NumberFormatException e) {
                throw new InventraInvalidTypeException(field, value, type);
            }
        case "d": // Date
            // Simple date validation, assuming the format is "dd/MM/yyyy"
            String[] parts = value.split("/");
            if (parts.length != 3) {
                throw new InventraInvalidTypeException(field, value, type);
            }
            try {
                int day = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int year = Integer.parseInt(parts[2]);
                if (day <= 0 || month <= 0 || month > 12) {
                    throw new InventraInvalidTypeException(field, value, type);
                }
                return null; // Valid date
            } catch (NumberFormatException e) {
                throw new InventraInvalidTypeException(field, value, type);
            }
        case "n": // Null
            if (!value.equalsIgnoreCase("null")) {
                throw new InventraInvalidTypeException(field, value, type);
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
