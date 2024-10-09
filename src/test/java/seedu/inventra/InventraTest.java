package seedu.inventra;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.command.AddCommand;
import seedu.model.Inventory;
import seedu.storage.Csv;
import seedu.ui.Ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


class InventraTest {
    @Test
    public void sampleTest() {
        assertTrue(true);
    }

    private Inventory inventory;
    private Ui ui;
    private Csv csv;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    public void setUp() {
        inventory = new Inventory();
        ui = new Ui();
        csv = new Csv();

        csv.initializeCsvFile(inventory);

        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    public void testAddAndListFields() {

        String[] addFieldsArgs = {"add", "-h", "s/name, i/quantity, f/price"};
        AddCommand addCommand = new AddCommand(inventory, ui, csv); // Pass Csv to AddCommand
        addCommand.execute(addFieldsArgs);

        // Capture output of add -l
        addCommand.execute(new String[]{"add", "-l"});

        String output = outputStream.toString();

        // Verify fields are correctly listed
        assertTrue(output.contains("| name") && output.contains("| quantity") && output.contains("| price"));
    }

    @Test
    public void testAddRecord() {
        // Setup add -h command to add fields
        String[] addFieldsArgs = {"add", "-h", "s/name, i/quantity, f/price"};
        AddCommand addCommand = new AddCommand(inventory, ui, csv); // Pass Csv to AddCommand
        addCommand.execute(addFieldsArgs);

        // Clear output before next command
        outputStream.reset();

        // Test add -d command to add record
        String[] addRecordArgs = {"add", "-d", "Apple, 10, 1.50"};
        addCommand.execute(addRecordArgs);

        // Capture output after adding record
        addCommand.execute(new String[]{"add", "-l"});

        String output = outputStream.toString();

        // Verify record is correctly added and displayed
        assertTrue(output.contains("| Apple") && output.contains("| 10") && output.contains("| 1.50"));
    }
}

