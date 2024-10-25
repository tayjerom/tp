package seedu.command;

import seedu.exceptions.InventraException;
import seedu.exceptions.InventraInvalidFlagException;
import seedu.exceptions.InventraInvalidTypeException;
import seedu.exceptions.InventraMissingFieldsException;
import seedu.exceptions.InventraInvalidRecordCountException;
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
        assert args != null && args.length >= 2 : "Arguments should contain at least a command and a flag";

        String flag = args[1];
        assert flag != null && !flag.isEmpty() : "Flag should not be null or empty";

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

        for (String field : fieldsToUpdate) {
            String[] parts = field.split("/");
            if (parts.length != 2) {
                ui.showErrorInvalidFieldFormat();
                return;
            }

            String type = parts[0].trim();
            String fieldName = parts[1].trim();

            if (inventory.getFields().contains(fieldName)) {
                ui.printMessage("    Field '" + fieldName + "' updated successfully with new type '" + type + "'.");
            } else {
                ui.printMessage("    Field '" + fieldName + "' added successfully.");
            }

            updatedFields.add(fieldName);
            updatedFieldTypes.put(fieldName, type);
        }

        inventory.setFields(updatedFields);
        inventory.setFieldTypes(updatedFieldTypes);

        csv.updateCsvHeaders(inventory);
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
                throw new InventraInvalidTypeException("Field format", field, "correct format (type/fieldName)");
            }

            String type = parts[0].trim();
            String fieldName = parts[1].trim();

            if (!isValidFieldType(type)) {
                throw new InventraInvalidTypeException(fieldName, type, "valid field type (e.g., 's', 'i', 'f')");
            }

            if (inventory.getFields().contains(fieldName)) {
                ui.printMessage("Field '" + fieldName + "' already exists. Cannot add duplicate headers.");
                success = false;
                continue;
            }

            inventory.addField(fieldName, type);
        }

        if (success) {
            ui.showSuccessFieldsAdded();
        } else {
            ui.printMessage("Failed to add one or more fields due to errors.");
        }

        ui.showFieldsAndRecords(inventory);
    }

    private void handleAddRecord(String recordData) throws InventraException {
        LOGGER.info("Handling add record: " + recordData);

        if (inventory.getFields().isEmpty()) {
            throw new InventraMissingFieldsException();
        }

        String[] values = recordData.split(",\\s*");

        if (values.length != inventory.getFields().size()) {
            throw new InventraInvalidRecordCountException(inventory.getFields().size(), values.length);
        }

        Map<String, String> record = new HashMap<>();
        for (int i = 0; i < values.length; i++) {
            String field = inventory.getFields().get(i);
            String type = inventory.getFieldTypes().get(field);

            assert type != null : "Type for field '" + field + "' should not be null";

            String value = values[i].trim();

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
        assert value != null && !value.isEmpty() : "Value should not be null or empty";
        assert type != null && !type.isEmpty() : "Field type should not be null or empty";
        assert field != null && !field.isEmpty() : "Field name should not be null or empty";

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
