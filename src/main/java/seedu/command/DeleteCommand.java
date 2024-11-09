package seedu.command;

import seedu.exceptions.InventraException;
import seedu.exceptions.InventraInvalidFlagException;
import seedu.exceptions.InventraInvalidNumberException;
import seedu.exceptions.InventraMissingArgsException;
import seedu.exceptions.InventraOutOfBoundsException;
import seedu.exceptions.InventraRangeOutOfBoundsException;
import seedu.model.Inventory;
import seedu.ui.Ui;
import seedu.storage.Csv;

import java.util.List;
import java.util.Map;

public class DeleteCommand extends Command {

    public DeleteCommand(Inventory inventory, Ui ui, Csv csv) {
        super(inventory, ui, csv);
    }

    public void execute(String[] args) throws InventraException {
        if (args.length < 2) {
            throw new InventraMissingArgsException("Record Number");
        }

        String part = args[1].trim();
        if (part.isEmpty()) {
            throw new InventraMissingArgsException("Record Number");
        }

        if (part.contains("-") && !part.startsWith("-")) {
            // If the input contains a range without the -r flag
            throw new InventraInvalidFlagException(
                    "Invalid usage of range. Use 'delete -r <start>-<end>'" +
                            " for deleting a range of records."
            );
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
                if (numbers.length != 2) {
                    throw new InventraInvalidFlagException("Invalid range format. Expected format: <start>-<end>");
                }
                deleteRangeRecords(parseIndex(numbers[0]), parseIndex(numbers[1]));
                break;
            default:
                throw new InventraInvalidFlagException("use 'help delete' to receive details about all " +
                        "available flags and their functions");
            }
        }

    }

    private void deleteEntireTable() {
        inventory.getFields().clear();
        inventory.getFieldTypes().clear();
        deleteAllRecords();
        csv.updateCsvAfterDeletion(inventory); // Update the CSV file to reflect the empty table
    }

    private boolean isWithinBounds(int index, int size) {
        return (index > 0 && index <= size);
    }

    private void deleteRangeRecords(int start, int end) throws InventraRangeOutOfBoundsException {
        List<Map<String, String>> records = inventory.getRecords();
        if (!isWithinBounds(start, records.size()) || !isWithinBounds(end, records.size())) {
            throw new InventraRangeOutOfBoundsException(start, end, 1, records.size());
        }
        if (end < start) {
            throw new InventraRangeOutOfBoundsException(start, end, 1, records.size());
        }
        inventory.getRecords().subList(start - 1, end).clear();
        csv.updateCsvAfterDeletion(inventory);
    }

    private void deleteAllRecords() {
        inventory.getRecords().clear();
        csv.updateCsvAfterDeletion(inventory); // Update the CSV file to reflect the empty records
    }

    private void deleteHeaderAndColumn(String fieldName) throws InventraInvalidFlagException {
        if (!inventory.getFields().contains(fieldName)) {
            throw new InventraInvalidFlagException("Header '"
                    + fieldName + "' does not exist.");
        }
        inventory.getFields().remove(fieldName);
        inventory.getFieldTypes().remove(fieldName);

        for (Map<String, String> record : inventory.getRecords()) {
            record.remove(fieldName);
        }
        csv.updateCsvAfterDeletion(inventory);
    }

    private int parseIndex(String indexString) throws InventraInvalidNumberException, InventraInvalidFlagException {
        if (indexString.contains(",") || indexString.contains("-")) {
            throw new InventraInvalidFlagException("Invalid flag or range format: "
                    + indexString);
        }
        try {
            return Integer.parseInt(indexString);
        } catch (NumberFormatException e) {
            throw new InventraInvalidNumberException(indexString);
        }
    }

    private void deleteSingleRecord(int recordIndex) throws InventraOutOfBoundsException {
        List<Map<String, String>> records = inventory.getRecords();

        if (isWithinBounds(recordIndex, records.size())) {
            records.remove(recordIndex - 1); // Convert to zero based indexing
            csv.updateCsvAfterDeletion(inventory);
        } else {
            throw new InventraOutOfBoundsException(recordIndex, 1, records.size());
        }
    }
}
