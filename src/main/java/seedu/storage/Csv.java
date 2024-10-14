package seedu.storage;

import seedu.model.Inventory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Csv {

    private final String csvFilePath;

    public Csv(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    // Initialize the CSV file or update headers when new fields are added
    public void updateCsvHeaders(Inventory inventory) {
        File file = new File(csvFilePath);

        try {
            // Ensure the storage directory exists
            File storageDir = new File("./storage");
            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }

            // Rewrite the header line based on the updated fields
            try (FileWriter writer = new FileWriter(file, false)) {
                List<String> fields = inventory.getFields();
                if (!fields.isEmpty()) {
                    writer.append(String.join(",", fields));
                    writer.append("\n");
                    System.out.println("CSV file headers updated at: " + file.getAbsolutePath());
                }
            }
        } catch (IOException e) {
            System.err.println("Error updating CSV file headers: " + e.getMessage());
        }
    }

    // Append a record to the CSV file considering the field order
    public void appendRecord(Map<String, String> record, Inventory inventory) {
        try (FileWriter writer = new FileWriter(csvFilePath, true)) {
            List<String> fields = inventory.getFields();
            for (int i = 0; i < fields.size(); i++) {
                String field = fields.get(i);
                String value = record.get(field);
                writer.append(value != null ? value : "null");  // null for empty fields
                if (i < fields.size() - 1) {
                    writer.append(",");
                }
            }
            writer.append("\n");
        } catch (IOException e) {
            System.err.println("Error appending record to CSV file: " + e.getMessage());
        }
    }
}
