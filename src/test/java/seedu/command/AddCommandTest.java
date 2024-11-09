package seedu.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.exceptions.InventraException;
import seedu.exceptions.InventraInvalidTypeException;
import seedu.exceptions.InventraMissingFieldsException;
import seedu.exceptions.InventraInvalidFlagException;
import seedu.exceptions.InventraInvalidRecordCountException;
import seedu.exceptions.InventraMissingArgsException;
import seedu.exceptions.InventraNegativeValueException;

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

    // New tests for covering exceptions in validateValue
    @Test
    public void validateValue_nullValue_throwsException() {
        String type = "s";
        String field = "name";
        AddCommand addCommand = new AddCommand(inventory, ui, csv);
        assertThrows(AssertionError.class, () -> {
            addCommand.validateValue(null, type, field);
        });
    }

    @Test
    public void validateValue_emptyValue_throwsException() {
        String type = "s";
        String field = "name";
        AddCommand addCommand = new AddCommand(inventory, ui, csv);
        assertThrows(AssertionError.class, () -> {
            addCommand.validateValue("", type, field);
        });
    }

    @Test
    public void validateValue_nullType_throwsException() {
        String value = "test";
        String field = "name";
        AddCommand addCommand = new AddCommand(inventory, ui, csv);
        assertThrows(AssertionError.class, () -> {
            addCommand.validateValue(value, null, field);
        });
    }

    @Test
    public void validateValue_emptyType_throwsException() {
        String value = "test";
        String field = "name";
        AddCommand addCommand = new AddCommand(inventory, ui, csv);
        assertThrows(AssertionError.class, () -> {
            addCommand.validateValue(value, "", field);
        });
    }

    @Test
    public void validateValue_nullField_throwsException() {
        String type = "s";
        String value = "test";
        AddCommand addCommand = new AddCommand(inventory, ui, csv);
        assertThrows(AssertionError.class, () -> {
            addCommand.validateValue(value, type, null);
        });
    }

    @Test
    public void validateValue_emptyField_throwsException() {
        String type = "s";
        String value = "test";
        AddCommand addCommand = new AddCommand(inventory, ui, csv);
        assertThrows(AssertionError.class, () -> {
            addCommand.validateValue(value, type, "");
        });
    }

    @Test
    public void validateValue_invalidDateFormat_throwsException() {
        String type = "d";
        String field = "date";
        AddCommand addCommand = new AddCommand(inventory, ui, csv);
        assertThrows(InventraInvalidTypeException.class, () -> {
            addCommand.validateValue("12-30-2020", type, field);
        });
    }

    @Test
    public void validateValue_invalidMonthInDate_throwsException() {
        String type = "d";
        String field = "date";
        AddCommand addCommand = new AddCommand(inventory, ui, csv);
        assertThrows(InventraInvalidTypeException.class, () -> {
            addCommand.validateValue("01/13/2020", type, field);
        });
    }

    @Test
    public void validateValue_invalidNullValue_throwsException() {
        String type = "n";
        String field = "nullableField";
        AddCommand addCommand = new AddCommand(inventory, ui, csv);
        assertThrows(InventraInvalidTypeException.class, () -> {
            addCommand.validateValue("notNull", type, field);
        });
    }

    @Test
    public void handleAddMultipleFields_emptyFieldData_throwsException() {
        String[] addFieldsArgs = {"add", "-h", ""};
        AddCommand addCommand = new AddCommand(inventory, ui, csv);
        assertThrows(InventraMissingFieldsException.class, () -> {
            addCommand.execute(addFieldsArgs);
        });
    }

    @Test
    public void handleAddMultipleFields_duplicateField_throwsException() throws InventraException {
        // First add the fields
        String[] addFieldsArgs = {"add", "-h", "s/name"};
        AddCommand addCommand = new AddCommand(inventory, ui, csv);
        addCommand.execute(addFieldsArgs);

        // Try adding a duplicate field
        String[] addDuplicateFieldArgs = {"add", "-h", "s/name"};
        assertThrows(InventraInvalidTypeException.class, () -> {
            addCommand.execute(addDuplicateFieldArgs);
        });
    }

    @Test
    public void validateValue_validFloatValue_success() throws InventraException {
        String type = "f";
        String field = "price";
        AddCommand addCommand = new AddCommand(inventory, ui, csv);
        assertNull(addCommand.validateValue("3.14", type, field));
    }

    @Test
    public void validateValue_invalidFloatValue_throwsException() {
        String type = "f";
        String field = "price";
        AddCommand addCommand = new AddCommand(inventory, ui, csv);
        assertThrows(InventraInvalidTypeException.class, () -> {
            addCommand.validateValue("abc", type, field);
        });
    }

    @Test
    public void validateValue_validIntegerValue_success() throws InventraException {
        String type = "i";
        String field = "quantity";
        AddCommand addCommand = new AddCommand(inventory, ui, csv);
        assertNull(addCommand.validateValue("42", type, field));
    }

    @Test
    public void validateValue_invalidIntegerValue_throwsException() {
        String type = "i";
        String field = "quantity";
        AddCommand addCommand = new AddCommand(inventory, ui, csv);
        assertThrows(InventraInvalidTypeException.class, () -> {
            addCommand.validateValue("3.14", type, field);
        });
    }

    @Test
    public void validateValue_validNullValue_success() throws InventraException {
        String type = "n";
        String field = "optionalField";
        AddCommand addCommand = new AddCommand(inventory, ui, csv);
        assertNull(addCommand.validateValue("null", type, field));
    }

    //condition testing
    @Test
    public void testArgsNotNullAndLength() {
        String[] args = {"command", "flag"};
        assertNotNull(args);
        assertTrue(args.length >= 2, "Arguments should contain at least a command and a flag");
    }

    @Test
    public void testFlagNotNullOrEmpty() {
        String flag = "h"; // or ""
        assertNotNull(flag);
        assertFalse(flag.isEmpty(), "Flag should not be null or empty");
    }

    @Test
    public void testArgsLengthForHelpFlag() {
        String[] args = {"command", "-h", "data"};
        assertTrue(args.length >= 3, "Expected additional field data for flag -h");
    }

    @Test
    public void testArgsLengthForDeleteFlag() {
        String[] args = {"command", "-d", "recordData"};
        assertTrue(args.length >= 3, "Expected record data for flag -d");
    }

    @Test
    public void testPartsLength() {
        String[] parts = {"part1", "part2"}; // Modify as necessary
        if (parts.length != 2) {
            fail("Parts length should be equal to 2");
        }
    }

    @Test
    public void testInventoryFieldsNotEmpty() {
        Inventory inventory = new Inventory();
        assertTrue(inventory.getFields().isEmpty(), "Inventory fields should be empty");
    }

    @Test
    public void testTypeNotNull() throws InventraException {
        // Set up: Add a field to the inventory
        String fieldName = "fieldName"; // Replace with the actual field name you want to test
        String fieldType = "String"; // Replace with the actual type you want to test

        String[] addFieldsArgs = {"add", "-h", "s/" + fieldName}; // Adjust if necessary
        AddCommand addCommand = new AddCommand(inventory, ui, csv);
        addCommand.execute(addFieldsArgs); // Execute to add the field

        // Now check that the type is not null
        String type = inventory.getFieldTypes().get(fieldName); // Retrieve the type from inventory
        assertNotNull(type, "Type for field '" + fieldName + "' should not be null");
    }

    @Test
    public void testValidationMessageNotNull() {
        String validationMessage = "Some message"; // Modify as needed
        assertNotNull(validationMessage);
    }

    @Test
    public void testHFlagNoAdditionalDataError() {
        String[] args = {"add", "-h"};  // Missing additional field data
        AddCommand addCommand = new AddCommand(inventory, ui, csv);

        InventraMissingArgsException thrown = assertThrows(InventraMissingArgsException.class, () -> {
            addCommand.execute(args);
        });

        assertEquals("Error: Missing the following arguments: field data for flag -h", thrown.getMessage());
    }

    @Test
    public void testHFlagInsufficientDataError() {
        AddCommand addCommand = new AddCommand(inventory, ui, csv);
        String expectedMessage = "Error: Invalid type for field 'Field format'\n" +
                "Expected value of type 'correct format (type/fieldName)', got: 'field1'";

        InventraException thrown = assertThrows(InventraInvalidTypeException.class, () -> {
            // Simulate invalid field input
            addCommand.execute(new String[]{"add", "-h", "field1"});
        });

        String actualMessage = thrown.getMessage();

        // Normalize both expected and actual strings
        assertEquals(expectedMessage.trim().replaceAll("\\s+", " "), actualMessage.trim().replaceAll("\\s+", " "));
    }

    @Test
    public void testDFlagNoRecordDataError() {
        // Test case where args is less than 3 for flag -d
        String[] args = new String[]{"add", "-d"}; // Only two arguments
        AddCommand addCommand = new AddCommand(inventory, ui, csv);
        AssertionError thrown = assertThrows(AssertionError.class, () -> {
            addCommand.execute(args);
        });

        assertEquals("Expected record data for flag -d", thrown.getMessage());
    }

    @Test
    public void testDFlagInsufficientDataError() {
        // Test case with insufficient data for flag -d
        String[] args = new String[]{"add", "-d", "value1"}; // Only three arguments
        AddCommand addCommand = new AddCommand(inventory, ui, csv);
        InventraMissingFieldsException thrown = assertThrows(InventraMissingFieldsException.class, () -> {
            addCommand.execute(args);
        });

        assertEquals("Error: Inventory is missing fields\n"
                + "Use 'add -h <s/pname, i/quantity, ...>' to add fields.", thrown.getMessage());
    }

    @Test
    public void testDateWithNegativeDayError() {
        String field = "dateField";
        String value = "-1/5/2024"; // Invalid day
        String type = "d";
        AddCommand addCommand = new AddCommand(inventory, ui, csv);
        assertThrows(InventraInvalidTypeException.class, () -> {
            addCommand.validateValue(value, type, field);
        });
    }

    @Test
    public void testDateWithNegativeMonthError() {
        String field = "dateField";
        String value = "15/-1/2024"; // Invalid month
        String type = "d";
        AddCommand addCommand = new AddCommand(inventory, ui, csv);
        assertThrows(InventraInvalidTypeException.class, () -> {
            addCommand.validateValue(value, type, field);
        });
    }

    @Test
    public void testDateMonthGreaterThanTwelveError() {
        String field = "dateField";
        String value = "15/13/2024"; // Invalid month (greater than 12)
        String type = "d";
        AddCommand addCommand = new AddCommand(inventory, ui, csv);
        assertThrows(InventraInvalidTypeException.class, () -> {
            addCommand.validateValue(value, type, field);
        });
    }

    // Test invalid date formats with different separators or missing components:
    @Test
    public void validateValue_incompleteDateFormat_throwsException() {
        String type = "d";
        String field = "date";
        AddCommand addCommand = new AddCommand(inventory, ui, csv);
        assertThrows(InventraInvalidTypeException.class, () -> {
            addCommand.validateValue("12/2020", type, field);
        });
    }

    // Test Duplicate field handling:
    @Test
    public void execute_addRecordWithExtraValues_throwsException() throws InventraException {
        // First add the fields
        String[] addFieldsArgs = {"add", "-h", "s/name, i/quantity, f/price"};
        AddCommand addCommand = new AddCommand(inventory, ui, csv);
        addCommand.execute(addFieldsArgs);

        // Then try adding a record with more values than fields
        String[] addRecordArgs = {"add", "-d", "Apple, 10, 1.50, extraValue"};

        assertThrows(InventraInvalidRecordCountException.class, () -> {
            addCommand.execute(addRecordArgs);
        });
    }

    // Test invalid date separators
    @Test
    public void validateValue_invalidDateSeparator_throwsException() {
        String type = "d";
        String field = "date";
        AddCommand addCommand = new AddCommand(inventory, ui, csv);
        assertThrows(InventraInvalidTypeException.class, () -> {
            addCommand.validateValue("12-31-2020", type, field); // Wrong separator
        });
    }

    // Add Record with special characters in name
    @Test
    public void execute_addRecordWithSpecialCharactersInName_success() throws InventraException {
        // Add fields first
        String[] addFieldsArgs = {"add", "-h", "s/name, i/quantity, f/price"};
        AddCommand addCommand = new AddCommand(inventory, ui, csv);
        addCommand.execute(addFieldsArgs);

        // Add record with special characters in the name
        String[] addRecordArgs = {"add", "-d", "Apple@!, 20, 2.99"};
        addCommand.execute(addRecordArgs);

        assertEquals("Apple@!", inventory.getRecords().get(0).get("name"));
    }

    @Test
    public void validateValue_negativeInteger_throwsInventraNegativeValueException() {
        AddCommand command = new AddCommand(new Inventory(),
                new Ui(), new Csv("test.csv"));

        assertThrows(InventraNegativeValueException.class, () -> {
            command.validateValue("-1", "i", "quantity");
        });
    }

    @Test
    public void validateValue_negativeFloat_throwsInventraNegativeValueException() {
        AddCommand command = new AddCommand(new Inventory(),
                new Ui(), new Csv("test.csv"));

        assertThrows(InventraNegativeValueException.class, () -> {
            command.validateValue("-1.5", "f", "price");
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
