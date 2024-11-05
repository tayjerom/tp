package seedu.command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.exceptions.InventraExcessArgsException;
import seedu.exceptions.InventraInvalidNumberException;
import seedu.exceptions.InventraMissingArgsException;
import seedu.exceptions.InventraOutOfBoundsException;
import seedu.model.Inventory;
import seedu.storage.Csv;
import seedu.ui.Ui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class ViewCommandTest {
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
    public void execute_missingArgs_throwsException() {
        String[] args = {"view"};
        assertThrows(InventraMissingArgsException.class,
                () -> new ViewCommand(inventory, ui).execute(args));
    }

    @Test
    public void execute_invalidNumber_throwsException() {
        String[] args = {"view", "invalidNumber"};
        assertThrows(InventraInvalidNumberException.class,
                () -> new ViewCommand(inventory, ui).execute(args));
    }

    @Test
    public void execute_excessArgsViewAll_throwsException() {
        String[] args = {"view", "-a", "extraArg"};
        assertThrows(InventraExcessArgsException.class,
                () -> new ViewCommand(inventory, ui).execute(args));
    }

    @Test
    public void execute_excessArgsViewSingle_throwsException() {
        String[] args = {"view", "1", "extraArg"};
        assertThrows(InventraExcessArgsException.class,
                () -> new ViewCommand(inventory, ui).execute(args));
    }


    @Test
    public void execute_missingArgsSearchKeyword_throwsException() {
        String[] args = {"view", "-f"};
        assertThrows(InventraMissingArgsException.class,
                () -> new ViewCommand(inventory, ui).execute(args));
    }


    @Test
    public void execute_outOfBoundsAccess_throwsException() {
        int recordCount = inventory.getRecords().size();

        for (int i = 0; i <= recordCount + 1; i++) {  // 0 and recordCount+1 should be out of bounds
            String input = "view " + i;
            String[] args = input.split(" ", 2);

            if (i == 0 || i > recordCount) {
                assertThrows(InventraOutOfBoundsException.class,
                        () -> new ViewCommand(inventory, ui).execute(args));
            }
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
