package seedu.command;
import seedu.model.Inventory;
import seedu.ui.Ui;
import java.util.List;
import java.util.Map;

public class ViewCommand {
    private Inventory inventory;
    private Ui ui;

    public ViewCommand(Inventory inventory, Ui ui) {
        this.inventory = inventory;
        this.ui = ui;
    }

    public void execute(String[] args) {
        if (args.length < 2) {
            ui.printViewHelp();
            return;
        }

        String flag = args[1];

        switch (flag) {
        case "-a":
            // View all items
            ui.showFieldsAndRecords(inventory);
            break;

        default:
            // Try to parse it as an ID to view a specific item
            try {
                int id = Integer.parseInt(flag);
                handleViewById(id);
            } catch (NumberFormatException e) {
                ui.printMessage("Error: Invalid ID format. Use an integer for record ID.");
            }
            break;
        }
    }

    private void handleViewById(int id) {
        List<Map<String, String>> records = inventory.getRecords();

        if (id <= 0 || id > records.size()) {
            ui.printMessage("Error: Record ID out of bounds.");
            return;
        }

        Map<String, String> record = records.get(id - 1); // Adjust for 0-based index
        ui.printSingleRecord(record, id);
    }
}
