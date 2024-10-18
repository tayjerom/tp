package seedu.ui;

import seedu.model.Inventory;
import seedu.parser.CommandParser;
import seedu.storage.Csv;

import java.util.Map;
import java.util.List;
import java.util.Scanner;

public class Ui {

    public void printGreeting(){
        String logo = " ___ _   ___     _______ _   _ _____ ____      _    \n"
                + "|_ _| \\ | \\ \\   / / ____| \\ | |_   _|  _ \\    / \\   \n"
                + " | ||  \\| |\\ \\ / /|  _| |  \\| | | | | |_) |  / _ \\  \n"
                + " | || |\\  | \\ V / | |___| |\\  | | | |  _ <  / ___ \\ \n"
                + "|___|_| \\_|  \\_/  |_____|_| \\_| |_| |_| \\_\\/_/   \\_\\\n";
        printMessage("Welcome to\n" + logo);
        printMessage("Type help to receive manual.");
    }

    public void run(Inventory inventory, Csv csv) {
        Scanner in = new Scanner(System.in);
        printGreeting();
        String input;
        do {
            input = in.nextLine();
            System.out.println("_____________________________________________");
            CommandParser.parseCommand(input, inventory, this, csv);
            System.out.println("_____________________________________________");
        } while (!input.equals("exit"));
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public void printSingleRecord(Map<String, String> record, int id) {
        printMessage("Record ID: " + id);
        for (Map.Entry<String, String> entry : record.entrySet()) {
            printMessage(entry.getKey() + ": " + entry.getValue());
        }
    }

    public void printViewHelp() {
        printMessage("Use [view -a] to view all records, or [view <ID>] to view individual record");
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

    public void showErrorInvalidFieldFormat() {
        printMessage("    Invalid field format. Use '<type>/<field>' for each field.");
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

    public void showUnknownTypeMessage(String type) {
        printMessage("Unknown field type: '" + type + "'. Valid types are:"
                + "s (String), i (Integer), f (Float), d (Date).");
    }

    public String getUnknownTypeMessage(String field) {
        return "    Unknown field type for field '" + field + "'.";
    }
}
