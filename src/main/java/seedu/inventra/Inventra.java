package seedu.inventra;

import seedu.model.Inventory;
import seedu.parser.CommandParser;
import seedu.ui.Ui;

import java.util.Scanner;

public class Inventra {
    private static Inventory inventory = new Inventory(); // Make inventory a static variable

    public static void main(String[] args) {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        System.out.println("Welcome to\n" + logo);

        Scanner in = new Scanner(System.in);
        Ui ui = new Ui();

        while (true) {
            System.out.println("Enter a command:");
            String input = in.nextLine();
            try {
                CommandParser.parseCommand(input, inventory, ui); // Pass inventory and ui
            } catch (Exception e) {
                ui.printMessage(e.getMessage());
            }
        }
    }
}
