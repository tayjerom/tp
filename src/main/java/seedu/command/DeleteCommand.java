package seedu.command;

import seedu.model.Inventory;
import java.util.List;
import java.util.Map;

public class DeleteCommand {

    public static void execute(String userInput, Inventory inventory) {
        String[] splitInput = userInput.split(" ");
        if (splitInput.length < 2) {
            System.out.println("    Error: No record number provided for deletion.");
            return;
        }

        try {
            int recordIndex = Integer.parseInt(splitInput[1]) - 1; // Convert to 0 index
            List<Map<String, String>> records = inventory.getRecords();

            if (recordIndex >= 0 && recordIndex < records.size()) {
                records.remove(recordIndex);
                System.out.println("    Record deleted successfully.");
            } else {
                System.out.println("    Error: Please provide a index within bounds");
            }
        } catch (NumberFormatException e) {
            System.out.println("    Error: Please provide a valid number");
        }
    }
}
