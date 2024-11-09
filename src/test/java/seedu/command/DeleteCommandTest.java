package seedu.command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.exceptions.InventraInvalidFlagException;
import seedu.exceptions.InventraInvalidNumberException;
import seedu.exceptions.InventraMissingArgsException;
import seedu.exceptions.InventraOutOfBoundsException;
import seedu.exceptions.InventraRangeOutOfBoundsException;
import seedu.model.Inventory;
import seedu.storage.Csv;
import seedu.ui.Ui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeleteCommandTest {
    private Inventory inventory;
    private Ui ui;
    private Csv csv;
    private String testCsvFilePath;

    @BeforeEach
    public void setUp() {
        inventory = new Inventory();
        ui = new Ui();
        // Set a unique CSV file path for testing
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
    public void execute_emptyInventory_throwsException() {
        // Reset inventory
        inventory = new Inventory();
        String input = "delete 1";
        String[] parts = input.split(" ", 3);
        assertThrows(InventraOutOfBoundsException.class, () -> new DeleteCommand(inventory, ui, csv).execute(parts));
    }

    @Test
    public void execute_outOfBoundsAccess_throwsException() {
        for (int i = 0; i < 10; ++i) {
            String input = "delete " + i;
            String[] parts = input.split(" ", 3);
            // 4 is number of record in sample1.csv, modify when modifying the sample file
            if (i == 0 || i > inventory.getRecords().size()) {
                assertThrows(InventraOutOfBoundsException.class,
                        () -> new DeleteCommand(inventory, ui, csv).execute(parts));
            }
        }
    }

    @Test
    public void execute_rangeOutOfBounds_throwsException() {
        for (int i = 0; i <= 6; ++i) {
            for (int j = 0; j <= 6; ++j) {
                String input = "delete -r " + i + "-" + j;
                String[] parts = input.split(" ", 3);
                if (i == 0 || j == 0 || i > inventory.getRecords().size() || j > inventory.getRecords().size() || i>j) {
                    assertThrows(InventraRangeOutOfBoundsException.class,
                            () -> new DeleteCommand(inventory, ui, csv).execute(parts));
                }
            }
        }
    }

    @Test
    public void execute_invalidNumberInput_throwsException() {
        String[] nonNumberInputs = {"Test", "1022Et$"};
        for (String nonNumberInput : nonNumberInputs) {
            String input = "delete " + nonNumberInput;
            String[] parts = input.split(" ", 3);
            assertThrows(InventraInvalidNumberException.class,
                    () -> new DeleteCommand(inventory, ui, csv).execute(parts));
        }
    }

    @Test
    public void execute_emptyArgument_throwsException() {
        String[] emptyInputs = {"", "      ", " -h ", " -r ", " -h", " -r"};
        for (String emptyInput : emptyInputs) {
            String input = "delete" + emptyInput;
            String[] parts = input.split(" ", 3);
            assertThrows(InventraMissingArgsException.class,
                    () -> new DeleteCommand(inventory, ui, csv).execute(parts));
        }
    }

    @Test
    public void execute_invalidFlags_throwsException() {
        String[] invalidFlags = {"-j", "-21"};
        for (String invalidFlag : invalidFlags) {
            String input = "delete " + invalidFlag;
            String[] parts = input.split(" ", 3);
            assertThrows(InventraInvalidFlagException.class,
                    () -> new DeleteCommand(inventory, ui, csv).execute(parts));
        }
    }

    @Test
    public void execute_deleteSingleRecord_success() {
        int originalSize = inventory.getRecords().size();
        Map<String, String> originalRecord = inventory.getRecords().get(0);
        String input = "delete 1";
        String[] parts = input.split(" ", 3);
        assertDoesNotThrow(() -> new DeleteCommand(inventory, ui, csv).execute(parts));
        assertEquals(originalSize - 1, inventory.getRecords().size());
        assertFalse(inventory.getRecords().contains(originalRecord));
    }

    @Test
    public void execute_deleteRangeRecords_success() {
        int originalSize = inventory.getRecords().size();
        String input = "delete -r 1-2";
        String[] parts = input.split(" ", 3);
        assertDoesNotThrow(() -> new DeleteCommand(inventory, ui, csv).execute(parts));
        assertEquals(originalSize - 2, inventory.getRecords().size());
    }

    @Test
    public void execute_deleteAllRecords_success() {
        String input = "delete -a";
        String[] parts = input.split(" ", 3);
        assertDoesNotThrow(() -> new DeleteCommand(inventory, ui, csv).execute(parts));
        assertEquals(0, inventory.getRecords().size());
    }

    @Test
    public void execute_deleteEntireTable_success() {
        String input = "delete -e";
        String[] parts = input.split(" ", 3);
        assertDoesNotThrow(() -> new DeleteCommand(inventory, ui, csv).execute(parts));
        assertEquals(0, inventory.getRecords().size());
        assertEquals(0, inventory.getFields().size());
        assertEquals(0, inventory.getFieldTypes().size());
    }

    @Test
    public void execute_deleteColumn_success() {
        String input = "delete -h quantity";
        String[] parts = input.split(" ", 3);
        assertDoesNotThrow(() -> new DeleteCommand(inventory, ui, csv).execute(parts));
        assertFalse(inventory.getFields().contains("quantity"));
        assertFalse(inventory.getFieldTypes().containsKey("quantity"));
        for (Map<String, String> record : inventory.getRecords()) {
            assertFalse(record.containsKey("quantity"));
        }
    }

    // Test for deleting header when it does exist:
    @Test
    public void execute_deleteNonExistentHeader_throwsException() {
        String input = "delete -h nonExistentField";
        String[] parts = input.split(" ", 3);
        assertThrows(InventraInvalidFlagException.class, ()
                -> new DeleteCommand(inventory, ui, csv).execute(parts));
    }

    // Test delete record from empty inventory:
    @Test
    public void execute_deleteRecordFromEmptyInventory_throwsException() {
        // Clear inventory records for testing
        inventory.getRecords().clear();
        String input = "delete 1";
        String[] parts = input.split(" ", 3);
        assertThrows(InventraOutOfBoundsException.class, ()
                -> new DeleteCommand(inventory, ui, csv).execute(parts));
    }

    // Test for invalid range format for deletion:
    @Test
    public void execute_invalidRangeFormat_throwsException() {
        String input = "delete -r 1,2"; // Incorrect format (comma instead of dash)
        String[] parts = input.split(" ", 3);
        assertThrows(InventraInvalidFlagException.class, ()
                -> new DeleteCommand(inventory, ui, csv).execute(parts));
    }

    @AfterEach
    public void tearDown() {
        File testFile = new File(testCsvFilePath);
        if (testFile.exists()) {
            testFile.delete();
        }
    }
}
