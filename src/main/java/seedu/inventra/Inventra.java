package seedu.inventra;

import seedu.model.Inventory;
import seedu.ui.Ui;
import seedu.storage.Csv;

public class Inventra {
    public static void main(String[] args) {
        Ui ui = new Ui();
        Inventory inventory = new Inventory(); // Instantiate Inventory here
        Csv csv = new Csv("data/inventory.csv");

        csv.loadInventoryFromCsv(inventory);

        ui.run(inventory, csv);
    }
}
