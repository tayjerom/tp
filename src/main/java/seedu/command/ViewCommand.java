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
        switch (flag) {
        case "-f":
            handleViewByKeyword(args);
            break;

        case "-a":
            if (args.length > 2) {
                throw new InventraExcessArgsException(2, args.length);
            }
            ui.showFieldsAndRecords(inventory); // View all items
            break;

        default:
            handleViewById(flag, args);
            break;
        }
    }

    private void handleViewById(String input, String[] args) throws InventraException {
        if (args.length > 2) {
            throw new InventraExcessArgsException(2, args.length);
        }

        try {
            int id = Integer.parseInt(input);

            if (id <= 0 || id > inventory.getRecords().size()) {
                throw new InventraOutOfBoundsException(id, 1, inventory.getRecords().size());
            }

            // Display the specific record based on the valid ID
            Map<String, String> record = inventory.getRecords().get(id - 1);
            ui.showFieldsAndRecords(inventory.getFields(), List.of(record));
        } catch (NumberFormatException e) {
            throw new InventraInvalidNumberException("Error: The input '"
                    + input + "' could not be parsed as an integer.");
        }
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
                if (inventory.isStringField(field) && record.get(field).toLowerCase().contains(keyword)) {
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
