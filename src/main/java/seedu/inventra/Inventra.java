package seedu.inventra;

import seedu.model.Inventory;
import seedu.parser.CommandParser;
import seedu.ui.Ui;
import seedu.storage.Csv;

import java.util.Scanner;

public class Inventra {
    public static void main(String[] args) throws Exception {
        String logo = " ___ _   ___     _______ _   _ _____ ____      _    \n"
                + "|_ _| \\ | \\ \\   / / ____| \\ | |_   _|  _ \\    / \\   \n"
                + " | ||  \\| |\\ \\ / /|  _| |  \\| | | | | |_) |  / _ \\  \n"
                + " | || |\\  | \\ V / | |___| |\\  | | | |  _ <  / ___ \\ \n"
                + "|___|_| \\_|  \\_/  |_____|_| \\_| |_| |_| \\_\\/_/   \\_\\\n";

        Ui ui = new Ui();
        ui.printMessage("Welcome to\n" + logo);
        ui.showUserManual();

        Scanner in = new Scanner(System.in);
        Inventory inventory = new Inventory(); // Instantiate Inventory here
        Csv csv = new Csv("src/main/java/seedu/storage/inventory.csv");

        // Load existing records from CSV
        csv.loadRecordsFromCsv(inventory);

        while (true) {
            String input = in.nextLine();
            try {
                System.out.println("_____________________________________________");
                CommandParser.parseCommand(input, inventory, ui, csv);
                System.out.println("_____________________________________________");
            } catch (Exception e) {
                ui.printMessage("    Error: " + e.getMessage());
            }
        }
    }
}
