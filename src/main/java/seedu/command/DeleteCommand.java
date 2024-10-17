package seedu.command;

import seedu.model.Inventory;
import seedu.ui.Ui;

import java.util.List;
import java.util.Map;

public class DeleteCommand {
    private final Inventory inventory;
    private final Ui ui;

    public DeleteCommand(Inventory inventory, Ui ui) {
        this.inventory = inventory;
        this.ui = ui;
    }

    public void execute(String[] parts) {
        if (parts.length < 2) {
            ui.printMessage("    Error: No record number provided for deletion.");
            return;
        }

        try {
            int recordIndex = Integer.parseInt(parts[1]) - 1; // Convert to 0 index
            List<Map<String, String>> records = inventory.getRecords();

            if (recordIndex >= 0 && recordIndex < records.size()) {
                records.remove(recordIndex);
                ui.printMessage("    Record deleted successfully.");
            } else {
                ui.printMessage("    Error: Please provide a index within bounds");
            }
        } catch (NumberFormatException e) {
            ui.printMessage("    Error: Please provide a valid number");
        }
    }
}
