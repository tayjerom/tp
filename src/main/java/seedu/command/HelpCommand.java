package seedu.command;

import java.util.LinkedHashMap;
import java.util.Map;

public class HelpCommand {

    private final Map<String,String> helpMessages;

    public HelpCommand() {
        // Initialize the help messages map
        helpMessages = new LinkedHashMap<>();

        // Add help messages for each command
        helpMessages.put("view",
                "View records:\n" +
                        "   - Command to view all items: view -a\n" +
                        "   - Command to view specific item by ID: view <ID>\n" +
                        "   - Example: view 1\n");

        helpMessages.put("add -h",
                "Add custom fields with specific types:\n" +
                        "Note: Types: s - String, i - Integer, f - Float, d - Date\n" +
                        "   - Command: add -h <type/field, type/field, ...>\n" +
                        "   - Example: add -h s/name, i/quantity, f/price\n");

        helpMessages.put("add -l",
                "Display all defined fields and stored records:\n" +
                        "   - Command: add -l\n");

        helpMessages.put("add -d",
                "Add Records to custom fields created:\n" +
                        "   - Command: add -d <value1, value2, ...>\n" +
                        "   - Example: add -d Apple, 100, 1.50, 01/10/2024\n");

        helpMessages.put("delete",
                "Delete Specific Records:\n" +
                        "   - Command: delete <record number>\n" +
                        "   - Example: delete 2\n");

        helpMessages.put("delete -a",
                "Delete All Records:\n" +
                        "   - Command: delete -a\n");

        helpMessages.put("delete -e",
                "Delete Entire Table:\n" +
                        "   - Command: delete -e\n");

        helpMessages.put("exit",
                "Exit program:\n" +
                        "   - Command: exit\n");
    }
    public void execute(String[] parts) {
        if (parts.length<2){
            int i = 1;
            for (String command : helpMessages.keySet()) {
                System.out.printf("%d. %s\n", i, helpMessages.get(command));
                i += 1;
            }
            return;
        }
        String command = parts[1];
        if (parts.length>2){
            command += " " + parts[2];
        }

        switch (command) {
        case "view":
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
            System.out.println(helpMessages.get("delete"));
            System.out.println(helpMessages.get("delete -a"));
            System.out.println(helpMessages.get("delete -e"));
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
