package seedu.command;

import seedu.ui.Ui;

public class ExitCommand {
    private final Ui ui;

    public ExitCommand(Ui ui) {
        this.ui = ui;
    }

    public void execute() {
        ui.printMessage("Program exit successfully.");
        System.exit(0);
    }
}
