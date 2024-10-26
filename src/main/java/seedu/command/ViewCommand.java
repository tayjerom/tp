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

public class ViewCommand {
    private final Inventory inventory;
    private final Ui ui;

    public ViewCommand(Inventory inventory, Ui ui) {
        this.inventory = inventory;
        this.ui = ui;
    }

    public void execute(String[] args) throws InventraException {

        String flag = args[1].trim();
        // Try to parse it as an ID to view a specific item
        if (flag.isEmpty()) {
            throw new InventraMissingArgsException("Item index");
        } else if (flag.equals("-f")) {
            handleViewByKeyword(args);
        } else {
            if (args.length > 2) {
                throw new InventraExcessArgsException(2, args.length);
            } else if (flag.equals("-a")) {// View all items
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

    }

    private void handleViewById(int id) throws InventraException {
        List<Map<String, String>> records = inventory.getRecords();
        if (id <= 0 || id >= records.size()) {
            throw new InventraOutOfBoundsException(id, 1, records.size());
        }
        Map<String, String> record = records.get(id - 1); // Adjust for 0-based index
        ui.printSingleRecord(record, id);
    }

    private void handleViewByKeyword(String[] args) throws InventraException {
        List<String> fields = inventory.getFields();
        Map<String, String> fieldtypes = inventory.getFieldTypes();
        List<Map<String, String>> records = inventory.getRecords();
        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            sb.append(args[i]);
            if (i != args.length - 1) { // Add a delimiter if it's not the last element
                sb.append(" ");
            }
        }
        String keyword = sb.toString();
        List<Map<String, String>> printedRecords = new ArrayList<>();
        List<Integer> printedRecordsIndexes = new ArrayList<>();
        for (String field : fields) {
            if (fieldtypes.get(field).equals("s")) {//check if the field is string type or not
                for (int i = 0; i < records.size(); i++) {
                    Map<String, String> record = records.get(i);
                    for (Map.Entry<String, String> entry : record.entrySet()) {
                        String key = entry.getKey();
                        if (field.equals(key)) {
                            if (entry.getValue().toLowerCase().contains(keyword.toLowerCase())) {
                                if (!printedRecords.contains(record)) {
                                    printedRecords.add(record);
                                    printedRecordsIndexes.add(i);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (!printedRecords.isEmpty()) {
            ui.printMessage("Here are the records that match the keyword");
            for (int i = 0; i < printedRecords.size(); i++) {
                Map<String, String> record = printedRecords.get(i);
                int id = printedRecordsIndexes.get(i);
                ui.printSingleRecord(record, id + 1);
                ui.printMessage("");
            }
        } else {
            ui.printMessage("Sorry there are no records that match the keyword");
        }
    }
}
