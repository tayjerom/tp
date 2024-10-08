package seedu.inventra;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Inventra {

    private static final List<String> fields = new ArrayList<>();
    private static final List<Map<String, String>> records = new ArrayList<>();

    public static void main(String[] args) {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        System.out.println("Welcome to\n" + logo);

        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println("Enter a command:");
            String input = in.nextLine();
            String[] inputParts = input.split(" ", 2);
            String command = inputParts[0];

            switch (command) {
            case "exit":
                System.out.println("Program exit successfully.");
                return;

            case "add":
                if (inputParts.length > 1 && inputParts[1].startsWith("-l")) {
                    String fieldData = inputParts[1].substring(2).trim();
                    handleAddMultipleFields(fieldData);

                } else if (inputParts.length > 1 && inputParts[1].startsWith("-d")) {
                    String recordData = inputParts[1].substring(2).trim();
                    handleAddRecord(recordData);

                } else {
                    System.out.println("Please check man-page for list of possible [add] commands");
                }
                break;

            case "view":
                viewRecords();
                break;

            default:
                System.out.println("Please input a valid command");
                break;
            }
        }
    }

    private static void handleAddMultipleFields(String fieldData) {
        if (fieldData.isEmpty()) {
            System.out.println("No fields provided. Try 'add -l field1, field2, ...' to add new fields.");
            return;
        }

        String[] newFields = fieldData.split(",\\s");
        for (String field : newFields) {
            fields.add(field.trim());
        }

        System.out.println("Fields added successfully");
        viewFields();
    }

    private static void handleAddRecord(String recordData) {
        if (fields.isEmpty()) {
            System.out.println("No fields have been defined yet. Use 'add -l' to create fields!");
        }

        String[] values = recordData.split(",\\s*");
        if (values.length != fields.size()) {
            System.out.println("Number of values provided does not match required fields.");
        }

        Map<String, String> record = new HashMap<>();
        for (int i = 0; i < fields.size(); i++) {
            record.put(fields.get(i), values[i]);
        }

        records.add(record);
        System.out.println("Record added successfully.");
    }

    private static void viewRecords() {
        viewFields();

        if (records.isEmpty()) {
            System.out.println("No records have been added yet");
        } else {
            System.out.println("Records:");
            for (int i = 0; i < records.size(); i++) {
                System.out.println("Record " + (i + 1) + ":");
                for (Map.Entry<String, String> entry : records.get(i).entrySet()) {
                    System.out.println(" - " + entry.getKey() + ": " + entry.getValue());
                }
            }
        }
    }

    private static void viewFields() {
        if (fields.isEmpty()) {
            System.out.println("No fields have been added yet.");
        } else {
            System.out.println("Fields: ");
            for (int i = 0; i < fields.size(); i++) {
                System.out.println((i + 1) + ". " + fields.get(i));
            }
        }
    }
}
