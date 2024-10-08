package seedu.ui;

import seedu.model.Inventory;
import java.util.Map;

public class Ui {
    public void printMessage(String message) {
        System.out.println(message);
    }

    public void showErrorInvalidCommand() {
        printMessage("Invalid command. Use 'add -h <fields>' to add fields, 'add -l' to list fields/records, or 'add -d <values>' to add records.");
    }

    public void showErrorNoFields() {
        printMessage("No fields provided. Use 'add -h <s/pname, i/quantity, ...>' to add fields.");
    }

    public void showErrorNoRecords() {
        printMessage("No record data provided. Use 'add -d <value1, value2, ...>' to add records.");
    }

    public void showErrorInvalidFlag() {
        printMessage("Invalid flag. Use 'add -h <fields>', 'add -l', or 'add -d <values>'");
    }

    public void showErrorInvalidFieldFormat() {
        printMessage("Invalid field format. Use '<type>/<field>' for each field.");
    }

    public void showErrorNoFieldsDefined() {
        printMessage("No fields defined. Use 'add -h <fields>' to define fields before adding records.");
    }

    public void showErrorInvalidRecordCount(int expected) {
        printMessage("Invalid number of values. Expected " + expected + " values.");
    }

    public void showSuccessFieldsAdded() {
        printMessage("Fields added successfully.");
    }

    public void showSuccessRecordAdded() {
        printMessage("Record added successfully.");
    }

    public void showFieldsAndRecords(Inventory inventory) {
        if (inventory.getFields().isEmpty()) {
            printMessage("No fields have been added yet.");
        } else {
            printMessage("Fields:");
            for (int i = 0; i < inventory.getFields().size(); i++) {
                String field = inventory.getFields().get(i);
                String type = inventory.getFieldTypes().get(field);
                printMessage((i + 1) + ". " + field + " (Type: " + type + ")");
            }
        }

        if (inventory.getRecords().isEmpty()) {
            printMessage("No records have been added yet.");
        } else {
            printMessage("Records:");
            for (Map<String, String> record : inventory.getRecords()) {
                StringBuilder recordStr = new StringBuilder();
                for (String field : inventory.getFields()) {
                    recordStr.append(field).append(": ").append(record.get(field)).append(", ");
                }
                recordStr.setLength(recordStr.length() - 2);  // Remove trailing comma and space
                printMessage(recordStr.toString());
            }
        }
    }

    public void showValidationError(String message) {
        printMessage(message);
    }

    // Validation error messages
    public String getInvalidIntegerMessage(String field, String value) {
        return "Invalid value for field '" + field + "'. Expected type: Integer, got: '" + value + "'";
    }

    public String getInvalidFloatMessage(String field, String value) {
        return "Invalid value for field '" + field + "'. Expected type: Float, got: '" + value + "'";
    }

    public String getInvalidDateMessage(String field, String value) {
        return "Invalid value for field '" + field + "'. Expected type: Date (format: dd/MM/yyyy), got: '" + value + "'";
    }

    public String getInvalidNullMessage(String field, String value) {
        return "Invalid value for field '" + field + "'. Expected type: Null, got: '" + value + "'";
    }

    public String getUnknownTypeMessage(String field) {
        return "Unknown field type for field '" + field + "'.";
    }
}
