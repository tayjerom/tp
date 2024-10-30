package seedu.command;

import seedu.model.Inventory;
import seedu.ui.Ui;
import seedu.storage.Csv;
import seedu.exceptions.InventraException;

public abstract class Command {
    protected Inventory inventory;
    protected Ui ui;
    protected Csv csv;

    public Command(Inventory inventory, Ui ui, Csv csv) {
        this.inventory = inventory;
        this.ui = ui;
        this.csv = csv;
    }

    public abstract void execute(String[] args) throws InventraException;
}
