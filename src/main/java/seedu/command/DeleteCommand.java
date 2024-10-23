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
            throw new InventraMissingArgsException("Record Number");
        }

        String part = args[1].trim();
        if (part.isEmpty()) {
            throw new InventraMissingArgsException("Record Number");
        }
        if (!part.startsWith("-")) {
            int index = parseIndex(part);
            deleteSingleRecord(index);
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
            case "-r":
                if (args.length < 3 || args[2].trim().isEmpty()) {
                    throw new InventraMissingArgsException("Range");
                }
                String[] numbers = args[2].trim().split("-");
                deleteRangeRecords(parseIndex(numbers[0]), parseIndex(numbers[1]));
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
        csv.updateCsvAfterDeletion(inventory); // Update the CSV file to reflect the empty table
    }

    private void deleteRangeRecords(int start,int end) throws InventraOutOfBoundsException {
        List<Map<String,String>> records = inventory.getRecords();
        if (!isWithinBounds(start,records.size())) {
            throw new InventraOutOfBoundsException(start,1,records.size());
        }
        if (!isWithinBounds(end,records.size())) {
            throw new InventraOutOfBoundsException(end,1,records.size());
        }
        if (end >= start) {
            inventory.getRecords().subList(start, end + 1).clear();
        }
    }

    private void deleteAllRecords() {
        inventory.getRecords().clear();
        csv.updateCsvAfterDeletion(inventory); // Update the CSV file to reflect the empty records
    }

    private void deleteHeaderAndColumn(String fieldName) {
        inventory.getFields().remove(fieldName);
        inventory.getFieldTypes().remove(fieldName);
        for (Map<String, String> record : inventory.getRecords()) {
            record.remove(fieldName);
        }
    }

    private int parseIndex (String indexString) throws InventraInvalidNumberException {
       try {
           return Integer.parseInt(indexString);
       }
       catch (NumberFormatException e) {
           throw new InventraInvalidNumberException(indexString);
       }
    }

    private boolean isWithinBounds(int index,int size){
        return (index > 0 && index<=size);
    }

    private void deleteSingleRecord(int recordIndex) throws InventraOutOfBoundsException {
        List<Map<String, String>> records = inventory.getRecords();

        if (isWithinBounds(recordIndex, records.size())) {
            records.remove(recordIndex-1); // Convert to zero based indexing
            csv.updateCsvAfterDeletion(inventory);
        } else {
            throw new InventraOutOfBoundsException(recordIndex, 1, records.size());
        }
    }
}
