package seedu.parser;

import seedu.command.AddCommand;
import seedu.command.DeleteCommand;
import seedu.command.HelpCommand;
import seedu.command.ViewCommand;
import seedu.exceptions.InventraException;
import seedu.exceptions.InventraInvalidCommandException;
import seedu.model.Inventory;
import seedu.ui.Ui;
import seedu.storage.Csv;

public class CommandParser {
    public static void parseCommand(String input, Inventory inventory, Ui ui, Csv csv) {
        String[] parts = input.split(" ", 3);
        String command = parts[0];

        try {
            switch (command) {
            case "add":
                new AddCommand(inventory, ui, csv).execute(parts);
                break;
            case "delete":
                new DeleteCommand(inventory, ui, csv).execute(parts);
                break;
            case "view":
                new ViewCommand(inventory, ui).execute(parts);
                break;
            case "help":
                new HelpCommand().execute();
                break;
            case "exit":
                ui.printMessage("Program exit successfully.");
                break;
            default:
                throw new InventraInvalidCommandException(command);
            }
        } catch (InventraException e) {
            System.out.println(e.getMessage());
        }
    }
}
