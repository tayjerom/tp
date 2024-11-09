package seedu.command;

import java.util.LinkedHashMap;
import java.util.Map;

import seedu.ui.Ui;

public class HelpCommand extends Command {

    private final Map<String, String> helpMessages;

    public HelpCommand(Ui ui) {
        super(null, ui, null); // Inventory and Csv are not needed here
        helpMessages = new LinkedHashMap<>();

        // Add help messages for each command
        helpMessages.put("view",
                "View records:\n" +
                        "   - Command to view all items: view -a\n" +
                        "   - Command to view specific item by ID: view <ID>\n" +
                        "   - Example: view 1\n" +
                        "   - Command to find specific item by string: view -f <STRING>\n" +
                        "   - Example: view -f Apple\n");

        helpMessages.put("add -h",
                "Add custom fields with specific types:\n" +
                        "Note: Types: s - String, i - Integer, f - Float, d - Date\n" +
                        "   - Command: add -h <type/field, type/field, ...>\n" +
                        "   - Example: add -h s/name, i/quantity, f/price, d/date\n");

        helpMessages.put("add -d",
                "Add Records to custom fields created:\n" +
                        "   - Command: add -d <value1, value2, ...>\n" +
                        "   - Example: add -d Apple, 100, 1.50, 01/10/2024\n" +
                        "   - Note: Date format must be DD/MM/YYYY or DD/MM/YY\n" +
                        "   - Example: add -d Apple, 100, 1.50, 01/10/2024\n");

        helpMessages.put("delete",
                "Delete Specific Records:\n" +
                        "   - Command: delete <record number>\n" +
                        "   - Example: delete 2\n\n" +
                        "Delete All Records:\n" +
                        "   - Command: delete -a\n\n" +
                        "Delete Entire Table:\n" +
                        "   - Command: delete -e\n\n" +
                        "Delete a header and its corresponding column:\n" +
                        "   - Command: delete -h <header_name>\n\n" +
                        "Delete a range of records using index range:\n" +
                        "   - Command: delete -r <start_index>-<end_index>\n");

        helpMessages.put("update",
                "Update Records or Fields:\n" +
                        "   - Update a record: update -d <index_number>,<field_name>,<new_value>\n" +
                        "   - Example: update -d 1,name,Orange\n" +
                        "   - Update a header: update -h <old_header_name>,<new_header_name>\n" +
                        "   - Example: update -h name,product_name\n");

        helpMessages.put("exit",
                "Exit program:\n" +
                        "   - Command: exit\n");
    }

    public void execute(String[] parts) {
        if (parts.length < 2) {
            int i = 1;
            for (String command : helpMessages.keySet()) {
                System.out.printf("%d. %s\n", i, helpMessages.get(command));
                i += 1;
            }
            return;
        }
        String command = parts[1];
        if (parts.length > 2) {
            command += " " + parts[2];
        }

        switch (command) {
        case "view":
        case "view -f":
            System.out.println(helpMessages.get("view"));
            break;

        case "add":
        case "add -h":
        case "add -l":
        case "add -d":
            System.out.println(helpMessages.get("add -h"));
            System.out.println(helpMessages.get("add -l"));
            System.out.println(helpMessages.get("add -d"));
            break;

        case "delete":
        case "delete -a":
        case "delete -e":
        case "delete -h":
        case "delete -r":
            System.out.println(helpMessages.get("delete"));
            break;

        case "update":
            System.out.println(helpMessages.get("update"));
            break;

        case "exit":
            System.out.println(helpMessages.get("exit"));
            break;

        default:
            System.out.println("Unknown command. Please use 'help <command>' for assistance.");
            break;
        }
    }
}
