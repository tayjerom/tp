package seedu.command;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.exceptions.InventraException;
import seedu.exceptions.InventraInvalidFlagException;
import seedu.exceptions.InventraInvalidTypeException;
import seedu.exceptions.InventraMissingFieldsException;
import seedu.model.Inventory;
import seedu.storage.Csv;
import seedu.ui.Ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

public class AddCommandTest {

    private Inventory inventory;
    private Ui ui;
    private Csv csv;
    private ByteArrayOutputStream outputStream;
    private String testCsvFilePath;

    @BeforeEach
    public void setUp() {
        inventory = new Inventory();
        ui = new Ui();
        testCsvFilePath = "data/test_inventory.csv";
        csv = new Csv(testCsvFilePath);

        File file = new File(testCsvFilePath);
        if (!file.exists()) {
            try (PrintStream writer = new PrintStream(file)) {
                writer.println("#name:s,quantity:i,price:f");
                writer.println("name,quantity,price");
            } catch (Exception e) {
                System.err.println("Error creating test CSV file: " + e.getMessage());
            }
        }

        csv.loadInventoryFromCsv(inventory);
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    public void execute_testAddMultipleFields_success() throws InventraException {
        String[] addFieldsArgs = {"add", "-h", "s/name, i/quantity, f/price"};
        AddCommand addCommand = new AddCommand(inventory, ui, csv);
        addCommand.execute(addFieldsArgs);

        assertTrue(inventory.getFields().contains("name"));
        assertTrue(inventory.getFieldTypes().get("name").equals("s"));
        assertTrue(inventory.getFields().contains("quantity"));
        assertTrue(inventory.getFields().contains("price"));
    }

    /*@Test
    public void execute_testAddMultipleFields_invalidFieldType_throwsException() throws InventraException {
        String[] addFieldsArgs = {"add", "-h", "x/unknownField"};
        AddCommand addCommand = new AddCommand(inventory, ui, csv);

        assertThrows(InventraInvalidTypeException.class, () -> {
            addCommand.execute(addFieldsArgs);
        });
    }*/

    @Test
    public void execute_testAddRecord_success() throws InventraException {
        // First add the fields
        String[] addFieldsArgs = {"add", "-h", "s/name, i/quantity, f/price"};
        AddCommand addCommand = new AddCommand(inventory, ui, csv);
        addCommand.execute(addFieldsArgs);

        // Then add a valid record
        String[] addRecordArgs = {"add", "-d", "Apple, 10, 1.50"};
        addCommand.execute(addRecordArgs);

        assertTrue(inventory.getRecords().get(0).get("name").equals("Apple"));
        assertTrue(inventory.getRecords().get(0).get("quantity").equals("10"));
        assertTrue(inventory.getRecords().get(0).get("price").equals("1.50"));
    }

    @Test
    public void execute_testAddRecord_invalidType_throwsException() throws InventraException {
        // First add the fields
        String[] addFieldsArgs = {"add", "-h", "s/name, i/quantity, f/price"};
        AddCommand addCommand = new AddCommand(inventory, ui, csv);
        addCommand.execute(addFieldsArgs);

        // Try adding a record with an invalid field type
        String[] addRecordArgs = {"add", "-d", "Apple, ten, 1.50"};

        assertThrows(InventraInvalidTypeException.class, () -> {
            addCommand.execute(addRecordArgs);
        });
    }

    @Test
    public void execute_testMissingFields_throwsException() {
        String[] addFieldsArgs = {"add", "-h", ""};
        AddCommand addCommand = new AddCommand(inventory, ui, csv);

        assertThrows(InventraMissingFieldsException.class, () -> {
            addCommand.execute(addFieldsArgs);
        });
    }

    @Test
    public void execute_testInvalidFlag_throwsException() {
        String[] invalidFlagArgs = {"add", "-x", "Some data"};
        AddCommand addCommand = new AddCommand(inventory, ui, csv);

        assertThrows(InventraInvalidFlagException.class, () -> {
            addCommand.execute(invalidFlagArgs);
        });
    }

    @AfterEach
    public void tearDown() {
        File testFile = new File(testCsvFilePath);
        if (testFile.exists()) {
            testFile.delete();
        }
    }
}
