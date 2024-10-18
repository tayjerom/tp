package seedu.command;

import seedu.exceptions.InventraException;
import seedu.exceptions.InventraInvalidNumberException;
import seedu.exceptions.InventraMissingArgsException;
import seedu.exceptions.InventraOutOfBoundsException;
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

    public void execute(String[] parts) throws InventraException {
        if (parts.length != 2) {
            ui.printViewHelp();
            return;
        }

        try {
            String part = parts[1].trim();
            if (part.isEmpty()) {
                throw new InventraMissingArgsException("Record Number");
            }
            int recordIndex = Integer.parseInt(part) - 1; // Convert to 0 index
            List<Map<String, String>> records = inventory.getRecords();

            if (recordIndex >= 0 && recordIndex < records.size()) {
                records.remove(recordIndex);
                csv.updateCsvAfterDeletion(inventory);
                ui.printMessage("    Record deleted successfully.");
            } else {
                throw new InventraOutOfBoundsException(recordIndex + 1, 1, records.size());
            }
        } catch (NumberFormatException e) {
            throw new InventraInvalidNumberException(parts[1]);
        }
    }
}
