package seedu.command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.exceptions.InventraInvalidFlagException;
import seedu.exceptions.InventraInvalidNumberException;
import seedu.exceptions.InventraMissingArgsException;
import seedu.exceptions.InventraOutOfBoundsException;
import seedu.model.Inventory;
import seedu.storage.Csv;
import seedu.ui.Ui;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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
        testCsvFilePath = "./src/main/java/seedu/storage/test_inventory.csv";
        csv = new Csv(testCsvFilePath);

        // Create the file if it does not exist
        File file = new File(testCsvFilePath);
        if (!file.exists()) {
            try (PrintStream writer = new PrintStream(file)) {
                writer.println("#name:s,quantity:i,price:f"); // Metadata header
                writer.println("name,quantity,price");         // Column headers
                writer.println("Apple,100,1.5");
                writer.println("Banana,300,3.5");
                writer.println("DragonFruit,200,5.0");
            } catch (Exception e) {
                System.err.println("Error creating test CSV file: " + e.getMessage());
            }
        }

        // Load existing records from CSV for the test (if any)
        csv.loadInventoryFromCsv(inventory);
    }

    @Test
    public void execute_emptyInventory_throwsException() {
        // Reset inventory
        inventory = new Inventory();
        String input = "delete 1";
        String[] parts = input.split(" ",3);
        assertThrows(InventraOutOfBoundsException.class, () -> new DeleteCommand(inventory, ui, csv).execute(parts));
    }

    @Test
    public void execute_outOfBoundsAccess_throwsException() {
        for (int i=0;i<10;++i){
            String input = "delete " + i;
            String[] parts = input.split(" ",3);
            // 4 is number of record in sample1.csv, modify when modifying the sample file
            if (i==0 || i>inventory.getRecords().size()){
                assertThrows(InventraOutOfBoundsException.class, () -> new DeleteCommand(inventory, ui, csv).execute(parts));
            }
        }
    }

    @Test
    public void execute_invalidNumberInput_throwsException() {
        String[] nonNumberInputs = {"Test","1022Et$"};
        for (String nonNumberInput : nonNumberInputs) {
            String input = "delete " + nonNumberInput;
            String[] parts = input.split(" ",3);
            assertThrows(InventraInvalidNumberException.class, () -> new DeleteCommand(inventory, ui, csv).execute(parts));
        }
    }

    @Test
    public void execute_emptyArgument_throwsException() {
        String[] emptyInputs = {"","      "};
        for (String emptyInput : emptyInputs) {
            String input = "delete" + emptyInput;
            String[] parts = input.split(" ",3);
            assertThrows(InventraMissingArgsException.class, () -> new DeleteCommand(inventory, ui, csv).execute(parts));
        }
    }

    @Test
    public void execute_invalidFlags_throwsException() {
        String[] invalidFlags = {"-j","-21"};
        for (String invalidFlag : invalidFlags) {
            String input = "delete " + invalidFlag;
            String[] parts = input.split(" ",3);
            assertThrows(InventraInvalidFlagException.class, () -> new DeleteCommand(inventory, ui, csv).execute(parts));
        }
    }

    @Test
    public void execute_deleteSingleRecord_success() {
        int originalSize = inventory.getRecords().size();
        Map<String,String> originalRecord = inventory.getRecords().get(0);
        String input = "delete 1";
        String[] parts = input.split(" ",3);
        assertDoesNotThrow(()-> new DeleteCommand(inventory,ui,csv).execute(parts));
        assertEquals(originalSize-1, inventory.getRecords().size());
        assertFalse(inventory.getRecords().contains(originalRecord));
    }

    @Test
    public void execute_deleteAllRecords_success() {
        String input = "delete -a";
        String[] parts = input.split(" ",3);
        assertDoesNotThrow(()-> new DeleteCommand(inventory,ui,csv).execute(parts));
        assertEquals(0,inventory.getRecords().size());
    }

    @Test
    public void execute_deleteEntireTable_success() {
        String input = "delete -e";
        String[] parts = input.split(" ",3);
        assertDoesNotThrow(()-> new DeleteCommand(inventory,ui,csv).execute(parts));
        assertEquals(0,inventory.getRecords().size());
        assertEquals(0,inventory.getFields().size());
        assertEquals(0,inventory.getFieldTypes().size());
    }

    @Test
    public void execute_deleteColumn_success(){
        String input = "delete -h quantity";
        String[] parts = input.split(" ",3);
        assertDoesNotThrow(()-> new DeleteCommand(inventory,ui,csv).execute(parts));
        assertFalse(inventory.getFields().contains("quantity"));
        assertFalse(inventory.getFieldTypes().containsKey("quantity"));
        for (Map<String,String> record : inventory.getRecords()){
            assertFalse(record.containsKey("quantity"));
        }
    }

    @AfterEach
    public void tearDown() {
        File testFile = new File(testCsvFilePath);
        if (testFile.exists()) {
            testFile.delete();
        }
    }
}
