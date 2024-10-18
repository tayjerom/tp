package seedu.command;

import seedu.exceptions.InventraException;
import seedu.exceptions.InventraInvalidFlagException;
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

    public void execute(String[] args) throws InventraException {
        if (args.length < 2) {
            ui.printViewHelp();
            return;
        }

        String part = args[1].trim();
        if (part.isEmpty()) {
            throw new InventraMissingArgsException("Record Number");
        }
        if (!part.startsWith("-")) {
            parseIndex(part);
            ui.printMessage("Record deleted successfully.");
        } else {
            switch (part) {
            case "-e":
                deleteEntireTable();
                ui.printMessage("Deleted entire table.");
                break;
            case "-a":
                deleteAllRecords();
                ui.printMessage("Deleted all records.");
                break;
            case "-h":
                if (args.length < 3 || args[2].trim().isEmpty()) {
                    throw new InventraMissingArgsException("Field name");
                }
                deleteHeaderAndColumn(args[2].trim());
                ui.printMessage("Deleted header and it's column.");
                break;
            default:
                throw new InventraInvalidFlagException("use delete -a to delete all records," +
                        "delete -e to delete entire table");
            }
        }

    }

    private void deleteEntireTable() {
        inventory.getFields().clear();
        inventory.getFieldTypes().clear();
        deleteAllRecords();
    }

    private void deleteAllRecords() {
        inventory.getRecords().clear();
    }

    private void deleteHeaderAndColumn(String fieldName) {
        inventory.getFields().remove(fieldName);
        inventory.getFieldTypes().remove(fieldName);
        for (Map<String, String> record : inventory.getRecords()) {
            record.remove(fieldName);
        }
    }

    private void parseIndex(String part) throws InventraOutOfBoundsException, InventraInvalidNumberException {
        try {
            int recordIndex = Integer.parseInt(part) - 1; // Convert to 0 index
            List<Map<String, String>> records = inventory.getRecords();

            if (recordIndex >= 0 && recordIndex < records.size()) {
                records.remove(recordIndex);
                csv.updateCsvAfterDeletion(inventory);
            } else {
                throw new InventraOutOfBoundsException(recordIndex + 1, 1, records.size());
            }
        } catch (NumberFormatException e) {
            throw new InventraInvalidNumberException(part);
        }
    }
}
