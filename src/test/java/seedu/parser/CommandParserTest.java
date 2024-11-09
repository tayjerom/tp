package seedu.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.model.Inventory;
import seedu.storage.Csv;
import seedu.ui.Ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommandParserTest {

    private Inventory inventory;
    private Ui ui;
    private Csv csv;
    private String testCsvFilePath;

    @BeforeEach
    void setUp() {
        inventory = new Inventory();
        ui = new Ui();
        testCsvFilePath = "data/test_inventory.csv";
        csv = new Csv(testCsvFilePath);
    }

    @Test
    void parseCommand_addCommand_executesSuccessfully() {
        String input = "add h field1 field2";

        assertDoesNotThrow(() -> CommandParser.parseCommand(input, inventory, ui, csv));
    }

    @Test
    void parseCommand_deleteCommand_executesSuccessfully() {
        String input = "delete 1";

        assertDoesNotThrow(() -> CommandParser.parseCommand(input, inventory, ui, csv));
    }

    @Test
    void parseCommand_viewCommand_executesSuccessfully() {
        String input = "view";

        assertDoesNotThrow(() -> CommandParser.parseCommand(input, inventory, ui, csv));
    }

    @Test
    void parseCommand_helpCommand_executesSuccessfully() {
        String input = "help";

        assertDoesNotThrow(() -> CommandParser.parseCommand(input, inventory, ui, csv));
    }

    @Test
    public void parseCommand_invalidCommand_printsErrorMessage() {

        String invalidCommand = "invalidCommand";

        // Capture the output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Act
        CommandParser.parseCommand(invalidCommand, inventory, ui, csv);

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Error: The command 'invalidCommand' is invalid. " +
                "Type help to receive valid commands"));

        // Reset the standard output
        System.setOut(System.out);
    }

    // Test for empty command:
    @Test
    void parseCommand_emptyCommand_noExceptionThrown() {
        String input = "";
        assertDoesNotThrow(() -> CommandParser.parseCommand(input, inventory, ui, csv));
    }

    // Test for case-insensitive commands:
    @Test
    void parseCommand_caseInsensitiveCommand_executesSuccessfully() {
        String input = "HeLp";
        assertDoesNotThrow(() -> CommandParser.parseCommand(input, inventory, ui, csv));
    }

    @Test
    void parseCommand_exitCommand_printsExitMessage() {
        String input = "exit";

        // Mock or capture UI output if necessary
        CommandParser.parseCommand(input, inventory, ui, csv);
    }
}
