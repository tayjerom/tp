package seedu.storage;

import seedu.model.Inventory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.List;

public class Csv {
    private static final String CSV_FILE_PATH = "src/main/java/seedu/storage/inventory.csv";

    public void initializeCsvFile(Inventory inventory) {
        File file = new File(CSV_FILE_PATH);

        // Create directories if they don't exist
        file.getParentFile().mkdirs();

        if (!file.exists()) {
            try (FileWriter writer = new FileWriter(file)) {
                // Write the header line based on inventory fields
                writer.append(String.join(",", inventory.getFields()));
                writer.append("\n");
                System.out.println("CSV file created at: " + file.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Error creating CSV file: " + e.getMessage());
            }
        }
    }

    public void appendRecord(Map<String, String> record) {
        try (FileWriter writer = new FileWriter(CSV_FILE_PATH, true)) {
            List<String> values = record.values().stream().map(Object::toString).toList();
            writer.append(String.join(",", values)).append("\n");
        } catch (IOException e) {
            System.out.println("Error appending record to CSV file: " + e.getMessage());
        }
    }
}