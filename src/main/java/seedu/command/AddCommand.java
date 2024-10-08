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
            ui.printMessage("Invalid command. Use 'add -h <fields>' to add fields or 'add -l' to list fields and records or 'add -d <values>' to add records.");
            return;
        }

        String flag = args[1];

        switch (flag) {
        case "-h":
            if (args.length < 3) {
                ui.printMessage("No fields provided. Use 'add -h <s/pname, i/quantity, ...>' to add fields.");
                return;
            }
            String fieldData = args[2];
            handleAddMultipleFields(fieldData);
            break;

        case "-l":
            listFieldsAndRecords();
            break;

        case "-d":
            if (args.length < 3) {
                ui.printMessage("No record data provided. Use 'add -d <value1, value2, ...>' to add records.");
                return;
            }
            String recordData = args[2];
            handleAddRecord(recordData);
            break;

        default:
            ui.printMessage("Invalid flag. Use 'add -h <fields>' or 'add -l' to list fields and records or 'add -d <values>' to add records.");
            break;
        }
    }

    private void handleAddMultipleFields(String fieldData) {
        if (fieldData.isEmpty()) {
            ui.printMessage("No fields provided. Use 'add -h <s/pname, i/quantity, ...>' to add fields.");
            return;
        }

        String[] newFields = fieldData.split(",\\s*");
        for (String field : newFields) {
            String[] parts = field.split("/");
            if (parts.length != 2) {
                ui.printMessage("Invalid field format. Use '<type>/<field>'.");
                return;
            }

            String type = parts[0].trim();
            String fieldName = parts[1].trim();
            inventory.addField(fieldName, type);
        }

        ui.printMessage("Fields added successfully.");
        listFieldsAndRecords();  // Show fields after adding
    }

    private void listFieldsAndRecords() {
        if (inventory.getFields().isEmpty()) {
            ui.printMessage("No fields have been added yet.");
        } else {
            ui.printMessage("Fields:");
            for (int i = 0; i < inventory.getFields().size(); i++) {
                String field = inventory.getFields().get(i);
                String type = inventory.getFieldTypes().get(field);
                ui.printMessage((i + 1) + ". " + field + " (Type: " + type + ")");
            }
        }

        if (inventory.getRecords().isEmpty()) {
            ui.printMessage("No records have been added yet.");
        } else {
            ui.printMessage("Records:");
            for (Map<String, String> record : inventory.getRecords()) {
                StringBuilder recordStr = new StringBuilder();
                for (String field : inventory.getFields()) {
                    recordStr.append(field).append(": ").append(record.get(field)).append(", ");
                }
                // Remove the trailing comma and space
                recordStr.setLength(recordStr.length() - 2);
                ui.printMessage(recordStr.toString());
            }
        }
    }

    private void handleAddRecord(String recordData) {
        if (inventory.getFields().isEmpty()) {
            ui.printMessage("No fields defined. Use 'add -h <fields>' to define fields before adding records.");
            return;
        }

        String[] values = recordData.split(",\\s*");
        if (values.length != inventory.getFields().size()) {
            ui.printMessage("Invalid number of values. Expected " + inventory.getFields().size() + " values.");
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
                ui.printMessage(validationMessage);
                return;
            }

            record.put(field, value);
        }

        inventory.addRecord(record);
        ui.printMessage("Record added successfully.");
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
                return "Invalid value for field '" + field + "'. Expected type: Integer (whole number), got: '" + value + "'";
            }
        case "f": // Float
            try {
                Float.parseFloat(value);
                return null; // Valid float
            } catch (NumberFormatException e) {
                return "Invalid value for field '" + field + "'. Expected type: Float (decimal number), got: '" + value + "'";
            }
        case "d": // Date
            // Simple date validation, assuming the format is "dd/MM/yyyy"
            String[] parts = value.split("/");
            if (parts.length != 3) {
                return "Invalid value for field '" + field + "'. Expected type: Date (format: dd/MM/yyyy), got: '" + value + "'";
            }
            try {
                int day = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int year = Integer.parseInt(parts[2]);
                if (day <= 0 || month <= 0 || month > 12) {
                    return "Invalid value for field '" + field + "'. Expected type: Date (format: dd/MM/yyyy), got: '" + value + "'";
                }
                return null; // Valid date
            } catch (NumberFormatException e) {
                return "Invalid value for field '" + field + "'. Expected type: Date (format: dd/MM/yyyy), got: '" + value + "'";
            }
        case "n": // Null
            if (!value.equalsIgnoreCase("null")) {
                return "Invalid value for field '" + field + "'. Expected type: Null (use 'null'), got: '" + value + "'";
            }
            return null; // Valid null
        default:
            return "Unknown field type for field '" + field + "'.";
        }
    }
}
