package seedu.ui;

import seedu.model.Inventory;
import seedu.parser.CommandParser;
import seedu.storage.Csv;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Scanner;

public class Ui {
    private static final int TERMINAL_WIDTH = 80;
    public void printGreeting() {
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
            input = in.nextLine().trim();  // Trim input to handle accidental spaces
            if (input.isEmpty()) {
                continue;  // Skip empty input lines
            }
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


    public void showFieldsAndRecords(Inventory inventory) {
        List<String> fields = inventory.getFields();
        List<Map<String, String>> records = inventory.getRecords();
        showFieldsAndRecords(fields, records);
    }

    public void showFieldsAndRecords(List<String> fields, List<Map<String, String>> records) {
        if (fields.isEmpty()) {
            printMessage("    No fields have been added yet.");
            return;
        }

        printTableHeader(fields, records);  // Pass both fields and records
        if (records.isEmpty()) {
            printMessage("    No records have been added yet.");
        } else {
            printTableRecords(fields, records);
        }
    }

    public void showSingleRecordWithOriginalId(List<String> fields, Map<String, String> record, int actualId) {
        StringBuilder header = new StringBuilder("    +");
        StringBuilder row = new StringBuilder("    | ");
        row.append(String.format("%-5d | ", actualId));  // Add correct ID

        for (String field : fields) {
            header.append("-".repeat(Math.max(field.length(), 20) + 2)).append("+");
            String value = record.getOrDefault(field, "null");
            row.append(String.format("%-20s | ", value));
        }
        printMessage(header.toString());
        printMessage(row.toString());
        printMessage(header.toString());
    }

    // Prints table header with fields and adds the "ID" column
    private void printTableHeader(List<String> fields, List<Map<String, String>> records) {
        StringBuilder header = new StringBuilder("    | ");
        StringBuilder separator = new StringBuilder("    +");

        // Add the "ID" column header
        int idColumnWidth = 5;  // Fixed width for ID column
        header.append(String.format("%-" + idColumnWidth + "s | ", "ID"));
        separator.append("-".repeat(idColumnWidth + 2)).append("+");

        // Add the other field headers
        for (String field : fields) {
            int maxFieldWidth = Math.max(
                    field.length(),
                    getMaxValueWidth(records, field)
            );
            header.append(String.format("%-" + maxFieldWidth + "s | ", field));
            separator.append("-".repeat(maxFieldWidth + 2)).append("+");
        }

        printMessage(separator.toString());
        printMessage(header.toString());
        printMessage(separator.toString());
    }

    private int getMaxValueWidth(List<Map<String, String>> records, String field) {
        return records.stream()
                .map(record -> record.getOrDefault(field, "null").length())
                .max(Integer::compare)
                .orElse(20); // Ensure a minimum width of 20
    }

    private void printTableRecords(List<String> fields, List<Map<String, String>> records) {
        StringBuilder separator = new StringBuilder("    +");

        // Fixed width for ID column
        int idColumnWidth = 5;
        separator.append("-".repeat(idColumnWidth + 2)).append("+");

        // Add the separator for the other fields dynamically
        Map<String, Integer> fieldWidths = new HashMap<>();
        for (String field : fields) {
            int maxFieldWidth = Math.max(
                    field.length(),
                    getMaxValueWidth(records, field)
            );
            separator.append("-".repeat(maxFieldWidth + 2)).append("+");
            fieldWidths.put(field, maxFieldWidth);
        }

        // Print the rows
        int id = 1;  // Start ID at 1
        for (Map<String, String> record : records) {
            StringBuilder row = new StringBuilder("    | ");

            // Print ID (fixed width of 5 characters)
            row.append(String.format("%-" + idColumnWidth + "d | ", id));
            id++;  // Increment ID for the next row

            // Print the other field values dynamically
            for (String field : fields) {
                String value = record.getOrDefault(field, "null");  // Handle missing values
                row.append(String.format("%-" + fieldWidths.get(field) + "s | ", value));  // Left-align values
            }

            printMessage(row.toString());
            printMessage(separator.toString());
        }
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

    public String getUnknownTypeMessage(String field) {
        return "    Unknown field type for field '" + field + "'.";
    }
}
