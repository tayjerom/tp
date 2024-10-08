package seedu.ui;

import seedu.model.Inventory;
import java.util.Map;
import java.util.List;

public class Ui {

    // Print a message to the user
    public void printMessage(String message) {
        System.out.println(message);
    }

    // Show fields and records in table format
    public void showFieldsAndRecords(Inventory inventory) {
        List<String> fields = inventory.getFields();
        List<Map<String, String>> records = inventory.getRecords();

        if (fields.isEmpty()) {
            printMessage("    No fields have been added yet.");
        } else {
            printTableHeader(fields);

            if (records.isEmpty()) {
                printMessage("    No records have been added yet.");
            } else {
                printTableRecords(fields, records);
            }
        }
    }

    // Prints table header with fields
    private void printTableHeader(List<String> fields) {
        StringBuilder header = new StringBuilder("    | ");
        StringBuilder separator = new StringBuilder("    +");

        for (String field : fields) {
            int columnWidth = Math.max(field.length(), 10);  // Ensure minimum column width of 10
            header.append(String.format("%-" + columnWidth + "s | ", field));  // Left-align the field names
            separator.append("-".repeat(columnWidth + 2)).append("+");  // Add separator line below header
        }

        printMessage(separator.toString());
        printMessage(header.toString());
        printMessage(separator.toString());
    }

    // Prints table rows with records
    private void printTableRecords(List<String> fields, List<Map<String, String>> records) {
        StringBuilder separator = new StringBuilder("    +");
        for (String field : fields) {
            int columnWidth = Math.max(field.length(), 10);
            separator.append("-".repeat(columnWidth + 2)).append("+");
        }

        for (Map<String, String> record : records) {
            StringBuilder row = new StringBuilder("    | ");
            for (String field : fields) {
                int columnWidth = Math.max(field.length(), 10);
                String value = record.getOrDefault(field, "null");  // Handle missing values
                row.append(String.format("%-" + columnWidth + "s | ", value));  // Left-align values
            }
            printMessage(row.toString());
            printMessage(separator.toString());
        }
    }

    // Error and success messages remain the same
    public void showErrorInvalidCommand() {
        printMessage("    Invalid command. Use 'add -h <fields>' to add fields, 'add -l' to list fields/records, or 'add -d <values>' to add records.");
    }

    public void showErrorNoFields() {
        printMessage("    No fields provided. Use 'add -h <s/pname, i/quantity, ...>' to add fields.");
    }

    public void showErrorNoRecords() {
        printMessage("    No record data provided. Use 'add -d <value1, value2, ...>' to add records.");
    }

    public void showErrorInvalidFlag() {
        printMessage("    Invalid flag. Use 'add -h <fields>', 'add -l', or 'add -d <values>'");
    }

    public void showErrorInvalidFieldFormat() {
        printMessage("    Invalid field format. Use '<type>/<field>' for each field.");
    }

    public void showErrorNoFieldsDefined() {
        printMessage("    No fields defined. Use 'add -h <fields>' to define fields before adding records.");
    }

    public void showErrorInvalidRecordCount(int expected) {
        printMessage("    Invalid number of values. Expected " + expected + " values.");
    }

    public void showSuccessFieldsAdded() {
        printMessage("    Fields added successfully.");
    }

    public void showSuccessRecordAdded() {
        printMessage("    Record added successfully.");
    }

    public void showValidationError(String message) {
        printMessage(message);
    }

    public String getInvalidIntegerMessage(String field, String value) {
        return "    Invalid value for field '" + field + "'. Expected type: Integer, got: '" + value + "'";
    }

    public String getInvalidFloatMessage(String field, String value) {
        return "    Invalid value for field '" + field + "'. Expected type: Float, got: '" + value + "'";
    }

    public String getInvalidDateMessage(String field, String value) {
        return "    Invalid value for field '" + field + "'. Expected type: Date (format: dd/MM/yyyy), got: '" + value + "'";
    }

    public String getInvalidNullMessage(String field, String value) {
        return "    Invalid value for field '" + field + "'. Expected type: Null, got: '" + value + "'";
    }

    public String getUnknownTypeMessage(String field) {
        return "    Unknown field type for field '" + field + "'.";
    }
}
