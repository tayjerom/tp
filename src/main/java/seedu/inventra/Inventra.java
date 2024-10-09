package seedu.inventra;

import seedu.model.Inventory;
import seedu.parser.CommandParser;
import seedu.ui.Ui;

import java.util.Scanner;

public class Inventra {
    public static void main(String[] args) {
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

        while (true) {
            String input = in.nextLine();
            try {
                System.out.println("_____________________________________________");
                CommandParser.parseCommand(input, inventory, ui);
                System.out.println("_____________________________________________");
            } catch (Exception e) {
                ui.printMessage("    Error: " + e.getMessage());
            }
        }
    }
}
