package seedu.parser;

import seedu.command.AddCommand;
import seedu.command.DeleteCommand;
import seedu.command.ExitCommand;
import seedu.command.ViewCommand;
import seedu.model.Inventory;
import seedu.ui.Ui;
import seedu.storage.Csv;

public class CommandParser {
    public static void parseCommand(String input, Inventory inventory, Ui ui, Csv csv)  {
        String[] parts = input.split(" ", 3);
        String command = parts[0];

        switch (command) {
        case "add":
            new AddCommand(inventory, ui, csv).execute(parts);
            break;
        case "delete":
            new DeleteCommand(inventory,ui).execute(parts);
            break;
        case "view":
            new ViewCommand(inventory, ui).execute(parts);
            break;
        case "exit":
            new ExitCommand(ui).execute();
            break;
        default:
            ui.showErrorInvalidCommand();
            break;
        }
    }
}
