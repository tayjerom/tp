package seedu.command;

public class HelpCommand {
    public void execute() {
        System.out.println("Inventra's User Manual:");
        System.out.println("Here is the list of commands you can use:");
        System.out.println("_____________________________________________");

        System.out.println("1. View records:");
        System.out.println("   - Command to view all items: view -a");
        System.out.println("   - Command to view specific item by ID: view <ID>");
        System.out.println("   - Example: view 1");

        System.out.println("2. Add custom fields with specific types:");
        System.out.println("Note: Types: s - String, i - Integer, f - Float, d - Date");
        System.out.println("   - Command: add -h <type/field, type/field, ...>");
        System.out.println("   - Example: add -h s/name, i/quantity, f/price");

        System.out.println(" ");
        System.out.println("3. Display all defined fields and stored records:");
        System.out.println("   - Command: add -l");

        System.out.println(" ");
        System.out.println("4. Add Records to custom fields created:");
        System.out.println("   - Command: add -d <value1, value2, ...>");
        System.out.println("   - Example: add -d Apple, 100, 1.50, 01/10/2024");

        System.out.println(" ");
        System.out.println("5. Delete Specific Records:");
        System.out.println("   - Command: delete <record number>");
        System.out.println("   - Example: delete 2");

        System.out.println(" ");
        System.out.println("6. Delete All Records:");
        System.out.println("   - Command: delete -a");

        System.out.println(" ");
        System.out.println("7. Delete Entire Table:");
        System.out.println("   - Command: delete -e");

        System.out.println(" ");
        System.out.println("8. Exit program:");
        System.out.println("   - Command: exit");
        System.out.println("_____________________________________________");
    }
}
