package seedu.command;
import seedu.exceptions.InventraException;
import seedu.exceptions.InventraInvalidNumberException;
import seedu.exceptions.InventraMissingArgsException;
import seedu.exceptions.InventraOutOfBoundsException;
import seedu.model.Inventory;
import seedu.ui.Ui;
import java.util.List;
import java.util.Map;

public class ViewCommand {
    private final Inventory inventory;
    private final Ui ui;

    public ViewCommand(Inventory inventory, Ui ui) {
        this.inventory = inventory;
        this.ui = ui;
    }

    public void execute(String[] args) throws InventraException {
        if (args.length != 2) {
            ui.printViewHelp();
            return;
        }

        String flag = args[1].trim();
        // Try to parse it as an ID to view a specific item
        if(flag.isEmpty()) {
            throw new InventraMissingArgsException("Item index");
        }
        if (flag.equals("-a")) {// View all items
            ui.showFieldsAndRecords(inventory);
        } else {
            try {
                int id = Integer.parseInt(flag);
                handleViewById(id);
            } catch (NumberFormatException e) {
                throw new InventraInvalidNumberException(flag);
            }
            // Out of bounds is not caught, will be directly thrown to commandParser to be handled.
        }
    }

    private void handleViewById(int id) throws InventraException {
        List<Map<String, String>> records = inventory.getRecords();
        if(id <= 0 || id >= records.size()) {
            throw new InventraOutOfBoundsException(id, 1, records.size());
        }
        Map<String, String> record = records.get(id - 1); // Adjust for 0-based index
        ui.printSingleRecord(record, id);
    }
}
