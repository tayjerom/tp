package seedu.inventra;

import seedu.model.Inventory;
import seedu.parser.CommandParser;
import seedu.ui.Ui;

import java.util.Scanner;

public class Inventra {
    private static Inventory inventory = new Inventory(); // Keep inventory static

    public static void main(String[] args) {
        String logo = " ___ _   ___     _______ _   _ _____ ____      _    \n"
                + "|_ _| \\ | \\ \\   / / ____| \\ | |_   _|  _ \\    / \\   \n"
                + " | ||  \\| |\\ \\ / /|  _| |  \\| | | | | |_) |  / _ \\  \n"
                + " | || |\\  | \\ V / | |___| |\\  | | | |  _ <  / ___ \\ \n"
                + "|___|_| \\_|  \\_/  |_____|_| \\_| |_| |_| \\_\\/_/   \\_\\\n";

        System.out.println("Welcome to\n" + logo);
        System.out.println("What would you like to do today?");
        System.out.println("_____________________________________________");

        Scanner in = new Scanner(System.in);
        Ui ui = new Ui();

        while (true) {
            // Print the prompt for user input
            String input = in.nextLine();
            // Separate user input and output
            printCommandOutput(input, ui, inventory);
        }
    }

    public static void printCommandOutput(String userInput, Ui ui, Inventory inventory) {
        // Clear distinction between input and output
        System.out.println("_____________________________________________");

        try {
            // Pass inventory and ui for parsing command
            CommandParser.parseCommand(userInput, inventory, ui);
        } catch (Exception e) {
            ui.printMessage("    Error: " + e.getMessage());
        }

        // Close the output with a line for clarity
        System.out.println("_____________________________________________");
    }
}
