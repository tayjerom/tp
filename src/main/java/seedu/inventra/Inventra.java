package seedu.inventra;

import seedu.model.Inventory;
import seedu.ui.Ui;
import seedu.storage.Csv;

public class Inventra {
    public static void main(String[] args){
        String logo = " ___ _   ___     _______ _   _ _____ ____      _    \n"
                + "|_ _| \\ | \\ \\   / / ____| \\ | |_   _|  _ \\    / \\   \n"
                + " | ||  \\| |\\ \\ / /|  _| |  \\| | | | | |_) |  / _ \\  \n"
                + " | || |\\  | \\ V / | |___| |\\  | | | |  _ <  / ___ \\ \n"
                + "|___|_| \\_|  \\_/  |_____|_| \\_| |_| |_| \\_\\/_/   \\_\\\n";

        Ui ui = new Ui();
        ui.printMessage("Welcome to\n" + logo);

        Inventory inventory = new Inventory(); // Instantiate Inventory here
        Csv csv = new Csv("src/main/java/seedu/storage/inventory.csv");

        // Load existing records from CSV
        csv.loadInventoryFromCsv(inventory);
        ui.run(inventory, csv);
    }
}
