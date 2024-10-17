package seedu.command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.model.Inventory;
import seedu.storage.Csv;
import seedu.ui.Ui;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

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
        testCsvFilePath = "./storage/test_inventory.csv";
        csv = new Csv(testCsvFilePath);
    }

    @Test
    public void execute_emptyInventory_throwsException() {
        try {
            String input = "delete 1";
            String[] parts = input.split(" ");
            new DeleteCommand(inventory,ui,csv).execute(parts);
            fail(); // the test should not reach this line
        } catch (Exception e) {
            assertEquals("Error: The inventory is empty.", e.getMessage());
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
