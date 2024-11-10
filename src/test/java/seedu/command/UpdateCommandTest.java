package seedu.command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.exceptions.InventraException;
import seedu.exceptions.InventraInvalidFlagException;
import seedu.exceptions.InventraInvalidTypeException;
import seedu.exceptions.InventraInvalidNumberException;
import seedu.exceptions.InventraOutOfBoundsException;
import seedu.exceptions.InventraExcessArgsException;
import seedu.exceptions.InventraLessArgsException;
import seedu.exceptions.InventraInvalidHeaderException;
import seedu.exceptions.InventraMissingArgsException;
import seedu.model.Inventory;
import seedu.storage.Csv;
import seedu.ui.Ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateCommandTest {
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
        try (FileWriter writer = new FileWriter(file)) { // Opens the file in overwrite mode by default
            writer.write("#name:s,quantity:i,price:f\n"); // Metadata header
            writer.write("name,quantity,price\n");         // Column headers
            writer.write("Apple,100,1.5\n");
            writer.write("Banana,300,3.5\n");
            writer.write("DragonFruit,200,5.0\n");
        } catch (IOException e) {
            System.err.println("Error creating test CSV file: " + e.getMessage());
        }
        csv.loadInventoryFromCsv(inventory);
    }

    @Test
    public void execute_updateField_success() throws InventraException {
        String[] updateFieldArgs = {"update", "-h", "name, newName"};
        UpdateCommand updateCommand = new UpdateCommand(inventory, ui, csv);
        updateCommand.execute(updateFieldArgs);

        List<String> fields = inventory.getFields();
        assertTrue(fields.contains("newName"));
        assertTrue(!fields.contains("name"));

        Map<String, String> fieldTypes = inventory.getFieldTypes();
        assertTrue(fieldTypes.containsKey("newName"));
        assertTrue(!fieldTypes.containsKey("name"));

        List<Map<String, String>> records = inventory.getRecords();
        for (Map<String, String> record : records) {
            assertTrue(record.containsKey("newName"));
            assertTrue(!record.containsKey("name"));
        }
    }

    @Test
    public void execute_updateFieldWithExcessArgs_throwsException() {
        String[] updateFieldArgs = {"update", "-h", "name,newName,extraArg"};
        UpdateCommand updateCommand = new UpdateCommand(inventory, ui, csv);

        assertThrows(InventraExcessArgsException.class, () -> {
            updateCommand.execute(updateFieldArgs);
        });
    }

    @Test
    void execute_updateFieldWithInvalidFormat_throwsMissingArgsException() {
        UpdateCommand command = new UpdateCommand(inventory, ui, csv);
        String input = "fieldAfieldB";  // Missing comma

        InventraMissingArgsException thrown = assertThrows(
                InventraMissingArgsException.class,
                () -> command.execute(new String[] { "update", "-h", input })
        );

        assertEquals("Error: Missing the following arguments:" +
                " Expected format: <old_field>, <new_field>.\n Ensure you separate " +
                "the old and new field names with a comma.", thrown.getMessage());
    }

    @Test
    void execute_updateFieldWithLessArgs_throwsLessArgsException() {
        UpdateCommand command = new UpdateCommand(inventory, ui, csv);
        String input = "fieldA,";  // Comma included, but missing second argument

        InventraLessArgsException thrown = assertThrows(
                InventraLessArgsException.class,
                () -> command.execute(new String[] { "update", "-h", input })
        );

        assertEquals("Error: Less arguments than required, expected: 2, received: 1", thrown.getMessage());
    }

    @Test
    public void execute_updateRecord_success() throws InventraException {
        String[] updateRecordArgs = {"update", "-d", "1, quantity, 20"};
        UpdateCommand updateCommand = new UpdateCommand(inventory, ui, csv);
        updateCommand.execute(updateRecordArgs);

        List<Map<String, String>> records = inventory.getRecords();
        assertEquals("20", records.get(0).get("quantity"));
    }

    @Test
    public void execute_updateRecordWithInvalidField_throwsException() {
        String[] updateRecordArgs = {"update", "-d", "1, nonExistentField, 20"};
        UpdateCommand updateCommand = new UpdateCommand(inventory, ui, csv);

        assertThrows(InventraInvalidHeaderException.class, () -> {
            updateCommand.execute(updateRecordArgs);
        });
    }

    @Test
    public void execute_updateRecordWithInvalidIndex_throwsException() {
        String[] updateRecordArgs = {"update", "-d", "0, price, 20"};
        UpdateCommand updateCommand = new UpdateCommand(inventory, ui, csv);

        assertThrows(InventraOutOfBoundsException.class, () -> {
            updateCommand.execute(updateRecordArgs);
        });
    }

    @Test
    public void execute_updateRecordWithLessArgs_throwsException() {
        String[] updateRecordArgs = {"update", "-d", "1,quantity"};
        UpdateCommand updateCommand = new UpdateCommand(inventory, ui, csv);

        assertThrows(InventraLessArgsException.class, () -> {
            updateCommand.execute(updateRecordArgs);
        });
    }

    @Test
    public void execute_updateRecordWithExcessArgs_throwsException() {
        String[] updateRecordArgs = {"update", "-d", "1, quantity, 20, extraArg"};
        UpdateCommand updateCommand = new UpdateCommand(inventory, ui, csv);

        assertThrows(InventraExcessArgsException.class, () -> {
            updateCommand.execute(updateRecordArgs);
        });
    }

    @Test
    public void execute_updateRecordWithInvalidNumber_throwsException() {
        String[] updateRecordArgs = {"update", "-d", "abc, quantity, 20"};
        UpdateCommand updateCommand = new UpdateCommand(inventory, ui, csv);

        assertThrows(InventraInvalidNumberException.class, () -> {
            updateCommand.execute(updateRecordArgs);
        });
    }

    @Test
    public void execute_updateRecordWithInvalidType_throwsException() {
        String[] updateRecordArgs = {"update", "-d", "1, quantity, twenty"};
        UpdateCommand updateCommand = new UpdateCommand(inventory, ui, csv);

        assertThrows(InventraInvalidTypeException.class, () -> {
            updateCommand.execute(updateRecordArgs);
        });
    }

    @Test
    public void execute_invalidFlag_throwsException() {
        String[] invalidFlagArgs = {"update", "-x", "some data"};
        UpdateCommand updateCommand = new UpdateCommand(inventory, ui, csv);

        assertThrows(InventraInvalidFlagException.class, () -> {
            updateCommand.execute(invalidFlagArgs);
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
