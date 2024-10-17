package seedu.command;

import seedu.model.Inventory;
import seedu.ui.Ui;
import seedu.storage.Csv;

import java.util.List;
import java.util.Map;

public class DeleteCommand {
    private final Inventory inventory;
    private final Ui ui;
    private final Csv csv;

    public DeleteCommand(Inventory inventory, Ui ui, Csv csv) {
        this.inventory = inventory;
        this.ui = ui;
        this.csv = csv;
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
                csv.updateCsvAfterDeletion(inventory);
                ui.printMessage("    Record deleted successfully.");
            } else {
                ui.printMessage("    Error: Please provide a index within bounds");
            }
        } catch (NumberFormatException e) {
            ui.printMessage("    Error: Please provide a valid number");
        }
    }
}
