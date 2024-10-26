package seedu.command;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.exceptions.InventraException;
import seedu.exceptions.InventraInvalidFlagException;
import seedu.exceptions.InventraInvalidTypeException;
import seedu.exceptions.InventraMissingFieldsException;
import seedu.exceptions.InventraInvalidRecordCountException;
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
    public void execute_addMultipleFields_success() throws InventraException {
        String[] addFieldsArgs = {"add", "-h", "s/name, i/quantity, f/price"};
        AddCommand addCommand = new AddCommand(inventory, ui, csv);
        addCommand.execute(addFieldsArgs);

        assertTrue(inventory.getFields().contains("name"));
        assertTrue(inventory.getFieldTypes().get("name").equals("s"));
        assertTrue(inventory.getFields().contains("quantity"));
        assertTrue(inventory.getFields().contains("price"));
    }

    @Test
    public void execute_addMultipleFieldsWithInvalidFieldType_throwsException() throws InventraException {
        String[] addFieldsArgs = {"add", "-h", "x/unknownField"};
        AddCommand addCommand = new AddCommand(inventory, ui, csv);

        assertThrows(InventraInvalidTypeException.class, () -> {
            addCommand.execute(addFieldsArgs);
        });
    }

    @Test
    public void execute_addRecord_success() throws InventraException {
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
    public void execute_addRecordOfInvalidType_throwsException() throws InventraException {
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
    public void execute_missingFields_throwsException() {
        String[] addFieldsArgs = {"add", "-h", ""};
        AddCommand addCommand = new AddCommand(inventory, ui, csv);

        assertThrows(InventraMissingFieldsException.class, () -> {
            addCommand.execute(addFieldsArgs);
        });
    }

    @Test
    public void execute_invalidFlag_throwsException() {
        String[] invalidFlagArgs = {"add", "-x", "Some data"};
        AddCommand addCommand = new AddCommand(inventory, ui, csv);

        assertThrows(InventraInvalidFlagException.class, () -> {
            addCommand.execute(invalidFlagArgs);
        });
    }

    @Test
    public void execute_updateFields_success() throws InventraException {
        String[] updateFieldsArgs = {"add", "-hu", "s/name, i/quantity"};
        AddCommand addCommand = new AddCommand(inventory, ui, csv);
        addCommand.execute(updateFieldsArgs);

        assertTrue(inventory.getFields().contains("name"));
        assertTrue(inventory.getFieldTypes().get("name").equals("s"));
        assertTrue(inventory.getFields().contains("quantity"));
        assertTrue(inventory.getFieldTypes().get("quantity").equals("i"));
    }

    /*@Test
    public void execute_testUpdateFields_invalidFormat_throwsException() throws InventraException {
        String[] updateFieldsArgs = {"add", "-hu", "invalidFieldFormat"};
        AddCommand addCommand = new AddCommand(inventory, ui, csv);

        // Assuming the UI method returns an error message for invalid field format
        addCommand.execute(updateFieldsArgs);
        String expectedOutput = "Error: Invalid field format. Expected format type/field.";
        assertTrue(outputStream.toString().contains(expectedOutput));
    }*/

    @Test
    public void execute_addRecordWithMismatchedFields_throwsException() throws InventraException {
        // Set up: add fields first
        String[] addFieldsArgs = {"add", "-h", "s/name, i/quantity, f/price"};
        AddCommand addCommand = new AddCommand(inventory, ui, csv);
        addCommand.execute(addFieldsArgs);

        // Try adding a record with too few values
        String[] addRecordArgs = {"add", "-d", "Apple, 10"};

        // Expect InventraInvalidRecordCountException to be thrown
        InventraInvalidRecordCountException exception = assertThrows(InventraInvalidRecordCountException.class, () -> {
            addCommand.execute(addRecordArgs);
        });

        // Verify the exception message
        String expectedMessage = "Error: Invalid number of values.\nExpected 3 values, but got 2.";
        assertEquals(expectedMessage, exception.getMessage());
    }


    @AfterEach
    public void tearDown() {
        File testFile = new File(testCsvFilePath);
        if (testFile.exists()) {
            testFile.delete();
        }
    }
}
