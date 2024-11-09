package seedu.command;

import seedu.exceptions.InventraException;
import seedu.exceptions.InventraInvalidFlagException;
import seedu.exceptions.InventraInvalidRecordCountException;
import seedu.exceptions.InventraInvalidTypeException;
import seedu.exceptions.InventraMissingArgsException;
import seedu.exceptions.InventraMissingFieldsException;
import seedu.exceptions.InventraNegativeValueException;
import seedu.model.Inventory;
import seedu.storage.Csv;
import seedu.ui.Ui;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class AddCommand extends Command {
    private static final Logger LOGGER = Logger.getLogger(AddCommand.class.getName());

    static {
        try {
            // Set up FileHandler to log messages to "app.log"
            FileHandler fileHandler = new FileHandler("app.log", true); // Appends to "app.log"
            fileHandler.setFormatter(new SimpleFormatter()); // Simple text formatter

            // Add FileHandler to the logger
            LOGGER.addHandler(fileHandler);

            // Disable logging to the console
            LOGGER.setUseParentHandlers(false);
        } catch (IOException e) {
            e.printStackTrace(); // Print exception if file handler fails to set up
        }
    }

    public AddCommand(Inventory inventory, Ui ui, Csv csv) {
        super(inventory, ui, csv);
    }

    public void execute(String[] args) throws InventraException {
        if (args.length < 2) {
            throw new InventraMissingArgsException("flag");
        }
        String flag = args[1];
        switch (flag) {
        case "-h":
            if (args.length < 3) {
                throw new InventraMissingArgsException("field data for flag -h");
            }
            handleAddMultipleFields(args[2]);
            csv.updateCsvHeaders(inventory);
            break;

        case "-d":
            assert args.length >= 3 : "Expected record data for flag -d";
            handleAddRecord(args[2]);
            csv.appendRecord(inventory.getRecords().get(inventory.getRecords().size() - 1), inventory);
            break;

        default:
            throw new InventraInvalidFlagException("Use 'add -h <fields>', or 'add -d <values>'");
        }
    }

    private void handleAddMultipleFields(String fieldData) throws InventraException {
        if (fieldData.isEmpty()) {
            throw new InventraMissingFieldsException();
        }

        String[] newFields = fieldData.split(",\\s*");

        for (String field : newFields) {
            String[] parts = field.split("/");
            if (parts.length != 2) {
                throw new InventraInvalidTypeException("Field format", field, "correct format (type/fieldName)");
            }

            String type = parts[0].trim();
            String fieldName = parts[1].trim();

            // Check if the field name is empty or contains only spaces
            if (fieldName.isEmpty()) {
                throw new InventraInvalidTypeException(fieldName, "cannot " +
                        "be empty or just spaces", "provide a valid field name");
            }

            if (fieldName.length() > 20) {
                throw new InventraInvalidTypeException(fieldName, "length exceeds 20 characters", "shorter name");
            }

            if (!isValidFieldType(type)) {
                throw new InventraInvalidTypeException(fieldName, type, "valid field type (e.g., 's', 'i', 'f', 'd')");
            }

            if (inventory.getFields().contains(fieldName)) {
                throw new InventraInvalidTypeException(fieldName, "duplicate field", "Field already exists");
            }

            inventory.addField(fieldName, type);
        }

        ui.showSuccessFieldsAdded();
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

            // Check if the value is empty or contains only spaces
            if (value.isEmpty()) {
                throw new InventraInvalidTypeException(field, "cannot " +
                        "be empty or just spaces", "provide a valid value for the record");
            }

            if (value.length() > 20) {
                throw new InventraInvalidTypeException(field, value, "length " +
                        "exceeds 20 characters");
            }

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


    public String validateValue(String value, String type, String field) throws InventraException {
        assert value != null && !value.isEmpty() : "Value should not be null or empty";
        assert type != null && !type.isEmpty() : "Field type should not be null or empty";
        assert field != null && !field.isEmpty() : "Field name should not be null or empty";

        switch (type) {
        case "s": // String
            if (value.matches("\\d+")) {
                throw new InventraInvalidTypeException(field, value, "non-numeric string");
            }
            return null; // Any string is valid

        case "i": // Integer
            try {
                if (value.length() > 9) { // Restrict integer length to 9 digits
                    throw new InventraInvalidTypeException(
                            String.format("Error: Invalid type for field '%s'%n" +
                                            "Expected value of type 'integer (up to 9 digits)', got: '%s'",
                                    field, value)
                    );
                }
                int intValue = Integer.parseInt(value); // Validates actual integer
                if (intValue < 0) {
                    throw new InventraNegativeValueException(field, value);
                }
                return null; // Valid integer
            } catch (NumberFormatException e) {
                throw new InventraInvalidTypeException(field, value, "integer");
            }

        case "f": // Float
            try {
                float floatValue = Float.parseFloat(value);
                if (floatValue < 0) {
                    throw new InventraNegativeValueException(field, value);
                }
                return null; // Valid float
            } catch (NumberFormatException e) {
                throw new InventraInvalidTypeException(field, value, "float");
            }

        case "d": // Date
            if (!value.matches("\\d{2}/\\d{2}/\\d{4}") && !value.matches("\\d{2}/\\d{2}/\\d{2}")) {
                throw new InventraInvalidTypeException(
                        field, value, "date (expected format: DD/MM/YYYY or DD/MM/YY)"
                );
            }
            String[] parts = value.split("/");
            try {
                int day = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int year = Integer.parseInt(parts[2]);
                if (day <= 0 || day > 31 || month <= 0 || month > 12 || year < 0) {
                    throw new InventraInvalidTypeException(
                            field, value, "valid date in DD/MM/YYYY or DD/MM/YY format"
                    );
                }
                return null; // Valid date
            } catch (NumberFormatException e) {
                throw new InventraInvalidTypeException(
                        field, value, "valid date (DD/MM/YYYY or DD/MM/YY)"
                );
            }

        case "n": // Null
            if (!value.equalsIgnoreCase("null")) {
                throw new InventraInvalidTypeException(field, value, "null");
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
