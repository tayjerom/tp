package seedu.command;

import seedu.exceptions.InventraException;

public abstract class Command {
    public abstract void execute(String[] args) throws InventraException;
}
