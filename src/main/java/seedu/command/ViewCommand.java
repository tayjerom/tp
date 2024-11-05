package seedu.command;

import seedu.exceptions.InventraException;
import seedu.exceptions.InventraExcessArgsException;
import seedu.exceptions.InventraInvalidNumberException;
import seedu.exceptions.InventraMissingArgsException;
import seedu.exceptions.InventraOutOfBoundsException;
import seedu.model.Inventory;
import seedu.ui.Ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewCommand extends Command {

    public ViewCommand(Inventory inventory, Ui ui) {
        super(inventory, ui, null);  // Passing `null` for unused dependencies
    }

    public void execute(String[] args) throws InventraException {
        if (args.length < 2) {
            throw new InventraMissingArgsException("Flag or Item index");
        }

        String flag = args[1].trim();
        if (flag.equals("-f")) {
            handleViewByKeyword(args);
        } else if (flag.equals("-a")) {
            if (args.length > 2) {
                throw new InventraExcessArgsException(2, args.length);
            }
            ui.showFieldsAndRecords(inventory); // View all items
        } else {
            try {
                int id = Integer.parseInt(flag);
                if (args.length > 2) {
                    throw new InventraExcessArgsException(2, args.length);
                }
                handleViewById(id);
            } catch (NumberFormatException e) {
                throw new InventraInvalidNumberException(flag);
            }
        }
    }

    private void handleViewById(int id) throws InventraException {
        List<Map<String, String>> records = inventory.getRecords();
        if (id <= 0 || id > records.size()) {
            throw new InventraOutOfBoundsException(id, 1, records.size());
        }
        ui.printSingleRecord(records.get(id - 1), id); // Adjust for 0-based index
    }

    private void handleViewByKeyword(String[] args) throws InventraException {
        if (args.length < 3 || args[2].trim().isEmpty()) {
            throw new InventraMissingArgsException("Keyword for filtering");
        }

        String keyword = String.join(" ",
                java.util.Arrays.copyOfRange(args, 2, args.length)).toLowerCase();
        List<Map<String, String>> records = inventory.getRecords();
        List<Map<String, String>> matchingRecords = new ArrayList<>();

        for (Map<String, String> record : records) {
            for (String field : record.keySet()) {
                if (inventory.isStringField(field) &&
                        record.get(field).toLowerCase().contains(keyword)) {
                    matchingRecords.add(record);
                    break;
                }
            }
        }

        if (!matchingRecords.isEmpty()) {
            ui.printMessage("Here are the records that match the keyword:");
            ui.showFieldsAndRecords(inventory.getFields(), matchingRecords);
        } else {
            ui.printMessage("Sorry, there are no records that match the keyword.");
        }
    }
}
