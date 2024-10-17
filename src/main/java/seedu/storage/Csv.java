package seedu.storage;

import seedu.model.Inventory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Csv {

    private final String csvFilePath;

    public Csv(String csvFilePath) {
        this.csvFilePath = csvFilePath;
        ensureDirectoryExists(); // Ensure the directory exists
    }

    // Ensure the directory exists
    private void ensureDirectoryExists() {
        File file = new File(csvFilePath);
        File parentDir = file.getParentFile(); // Get the parent directory
        if (parentDir != null && !parentDir.exists()) {
            if (parentDir.mkdirs()) {
                System.out.println("Directory created: " + parentDir.getAbsolutePath());
            } else {
                System.err.println("Failed to create directory: " + parentDir.getAbsolutePath());
            }
        }
    }

    // Update the CSV file headers and keep the existing data
    public void updateCsvHeaders(Inventory inventory) {
        File file = new File(csvFilePath);
        List<String> existingRecords = new ArrayList<>();

        // Read existing data, if any
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                // Skip the old header
                reader.readLine();
                // Read the existing records
                String line;
                while ((line = reader.readLine()) != null) {
                    existingRecords.add(line);
                }
            } catch (IOException e) {
                System.err.println("Error reading existing CSV file: " + e.getMessage());
            }
        }

        try {
            // Overwrite the file with updated headers and preserve records
            try (FileWriter writer = new FileWriter(file, false)) {
                // Write updated headers
                List<String> fields = inventory.getFields();
                if (!fields.isEmpty()) {
                    writer.append(String.join(",", fields));
                    writer.append("\n");
                }

                // Write back the existing records
                for (String record : existingRecords) {
                    writer.append(record).append("\n");
                }

                System.out.println("CSV file headers updated with existing records preserved.");
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

    // Load records from the CSV file into the Inventory
    public void loadRecordsFromCsv(Inventory inventory) {
        File file = new File(csvFilePath);
        if (!file.exists()) {
            System.out.println("CSV file does not exist. Starting with an empty inventory.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Read header
            String headerLine = reader.readLine();
            if (headerLine == null) {
                System.out.println("CSV file is empty. No records to load.");
                return;
            }
            List<String> fields = Arrays.asList(headerLine.split(","));
            inventory.setFields(fields);

            // Read each record and add to the Inventory
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                Map<String, String> record = new HashMap<>();
                for (int i = 0; i < fields.size(); i++) {
                    record.put(fields.get(i), i < values.length ? values[i].trim() : null);
                }
                inventory.addRecord(record);  // Add record to inventory
            }
            System.out.println("Records loaded from CSV file.");
        } catch (IOException e) {
            System.err.println("Error loading records from CSV file: " + e.getMessage());
        }
    }
}
