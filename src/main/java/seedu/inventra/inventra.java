package seedu.inventra;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class inventra {

    private static List<String> fields = new ArrayList<>();
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
                    } else {
                        System.out.println("Please check man-page for list of possible [add] commands");
                    }
                    break;
                
                case "view":
                    viewFields();
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
