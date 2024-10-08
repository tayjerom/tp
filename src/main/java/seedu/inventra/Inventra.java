package seedu.inventra;

import seedu.model.Inventory;
import seedu.parser.CommandParser;
import seedu.ui.Ui;

import java.util.Scanner;

public class Inventra {
    private static Inventory inventory = new Inventory(); // Make inventory a static variable

    public static void main(String[] args) {
        String logo = " ___ _   ___     _______ _   _ _____ ____      _    \n"
                + "|_ _| \\ | \\ \\   / / ____| \\ | |_   _|  _ \\    / \\   \n"
                + " | ||  \\| |\\ \\ / /|  _| |  \\| | | | | |_) |  / _ \\  \n"
                + " | || |\\  | \\ V / | |___| |\\  | | | |  _ <  / ___ \\ \n"
                + "|___|_| \\_|  \\_/  |_____|_| \\_| |_| |_| \\_\\/_/   \\_\\\n";
        System.out.println("Welcome to\n" + logo);

        Scanner in = new Scanner(System.in);
        Ui ui = new Ui();

        while (true) {
            String input = in.nextLine();
            try {
                CommandParser.parseCommand(input, inventory, ui); // Pass inventory and ui
            } catch (Exception e) {
                ui.printMessage(e.getMessage());
            }
        }
    }
}
