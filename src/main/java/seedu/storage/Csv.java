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

    // Initialize the CSV file
    public void initializeCsvFile(Inventory inventory) {
        File file = new File(csvFilePath);

        try {
            // Ensure the storage directory exists
            File storageDir = new File("./storage");
            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }

            // If the CSV file does not exist, create it and write the headers
            if (!file.exists()) {
                boolean fileCreated = file.createNewFile();
                if (fileCreated) {
                    try (FileWriter writer = new FileWriter(file)) {
                        List<String> fields = inventory.getFields();
                        if (!fields.isEmpty()) {
                            writer.append(String.join(",", fields));
                            writer.append("\n");
                            System.out.println("CSV file created at: " + file.getAbsolutePath());
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error during CSV initialization: " + e.getMessage());
        }
    }

    // Append a record to the CSV file
    public void appendRecord(Map<String, String> record, Inventory inventory) {
        try (FileWriter writer = new FileWriter(csvFilePath, true)) {
            // Iterate over the fields in the correct order
            List<String> fields = inventory.getFields();
            for (int i = 0; i < fields.size(); i++) {
                String field = fields.get(i);
                String value = record.get(field);  // Fetch the value for the field
                writer.append(value != null ? value : "");
                if (i < fields.size() - 1) {
                    writer.append(",");  // Add commas between values
                }
            }
            writer.append("\n");
        } catch (IOException e) {
            System.err.println("Error appending record to CSV file: " + e.getMessage());
        }
    }
}
