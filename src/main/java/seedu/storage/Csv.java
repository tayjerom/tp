package seedu.storage;

import seedu.model.Inventory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Csv {

    private final String csvFilePath;

    public Csv(String relativeCsvFilePath) {
        this.csvFilePath = relativeCsvFilePath;
        ensureParentDirectoriesExist(); // Ensure the directory exists
        ensureFileExists();
    }

    // Ensure the directory exists
    private void ensureParentDirectoriesExist() {
        File file = new File(csvFilePath);
        File parentDir = file.getParentFile(); // Get the parent directory
        if (parentDir != null && !parentDir.exists()) {
            if (parentDir.mkdirs()) {
                System.out.println("Directory created: " + parentDir.getName());
            } else {
                System.err.println("Failed to create directory: " + parentDir.getName());
            }
        }
    }

    private void ensureFileExists() {
        File file = new File(csvFilePath);
        try {
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            }
        } catch (IOException e) {
            System.err.println("Failed to create file: " + e.getMessage());
        }
    }

    public void updateCsvAfterDeletion(Inventory inventory) {
        List<String> fields = inventory.getFields();
        Map<String, String> fieldTypes = inventory.getFieldTypes();

        // Create the metadata line from field types
        List<String> metadata = new ArrayList<>();
        for (String field : fields) {
            String type = fieldTypes.get(field);
            metadata.add(field + ":" + type);
        }
        String metadataLine = "#" + String.join(",", metadata);

        try (FileWriter writer = new FileWriter(csvFilePath, false)) {
            // Write the metadata line first
            writer.append(metadataLine).append("\n");

            // Write field names (headers)
            if (!fields.isEmpty()) {
                writer.append(String.join(",", fields)).append("\n");
            }

            // Handle records
            for (Map<String, String> record : inventory.getRecords()) {
                for (int i = 0; i < fields.size(); i++) {
                    String field = fields.get(i);
                    String value = record.getOrDefault(field, "null");
                    writer.append(value != null ? value : "null");
                    if (i < fields.size() - 1) {
                        writer.append(",");
                    }
                }
                writer.append("\n");
            }
            System.out.println("CSV file updated after deletion, including metadata.");
        } catch (IOException e) {
            System.err.println("Error updating CSV after deletion: " + e.getMessage());
        }
    }

    // Update the CSV file headers and keep the existing data
    public void updateCsvHeaders(Inventory inventory) {
        List<String> fields = inventory.getFields();
        Map<String, String> fieldTypes = inventory.getFieldTypes();

        // metadata include for persistent field type
        List<String> metadata = new ArrayList<>();
        for (String field : fields) {
            String type = fieldTypes.get(field);
            metadata.add(field + ":" + type);
        }
        String metadataLine = "#" + String.join(",", metadata);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
            // Write metadata line
            writer.write(metadataLine);
            writer.newLine();
            // Write header line (field names)
            writer.write(String.join(",", fields));
            writer.newLine();
            // Write data records
            for (Map<String, String> record : inventory.getRecords()) {
                List<String> values = new ArrayList<>();
                for (String field : fields) {
                    values.add(record.getOrDefault(field, ""));
                }
                writer.write(String.join(",", values));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Append a record to the CSV file considering the field order
    public void appendRecord(Map<String, String> record, Inventory inventory) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath, true))) {
            List<String> values = new ArrayList<>();
            for (String field : inventory.getFields()) {
                values.add(record.getOrDefault(field, ""));
            }
            writer.write(String.join(",", values));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load records from the CSV file into the Inventory
    public void loadInventoryFromCsv(Inventory inventory) {
        File file = new File(csvFilePath);
        if (file.length() == 0) {
            // No data to load, fresh file
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            // Read field definitions from the metadata header (first line starting with #)
            if ((line = reader.readLine()) != null && line.startsWith("#")) {
                String metadata = line.substring(1);  // Remove the # character
                String[] fieldsWithTypes = metadata.split(",\\s*");
                List<String> fields = new ArrayList<>();
                Map<String, String> fieldTypes = new HashMap<>();

                for (String fieldType : fieldsWithTypes) {
                    String[] parts = fieldType.trim().split(":");
                    if (parts.length == 2) {
                        String fieldName = parts[0].trim();
                        String type = parts[1].trim();
                        fields.add(fieldName);
                        fieldTypes.put(fieldName, type);
                    } else {
                        System.out.println("Invalid field type definition: " + fieldType);
                        return;
                    }
                }

                inventory.setFields(fields);
                inventory.setFieldTypes(fieldTypes);
                System.out.println("Fields loaded from CSV: " + fields);
                System.out.println("Field types loaded from CSV: " + fieldTypes);
            } else {
                System.out.println("CSV file format error: Missing metadata header.");
                return;
            }

            // Read headers (second line)
            if ((line = reader.readLine()) != null) {
                String[] headers = line.split(",");
                System.out.println("Column headers: " + String.join(", ", headers));
            }

            // Read records (remaining lines)
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                Map<String, String> record = new HashMap<>();

                List<String> fields = inventory.getFields();
                for (int i = 0; i < values.length; i++) {
                    if (i < fields.size()) {
                        String field = fields.get(i).trim();
                        String value = values[i].trim();
                        record.put(field, value);
                    }
                }

                inventory.addRecord(record);
                System.out.println("Record added: " + record);
            }

            System.out.println("Finished loading CSV file.");
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateCsv(Inventory inventory) {
        List<String> fields = inventory.getFields();
        Map<String, String> fieldTypes = inventory.getFieldTypes();

        // Create the metadata line from field types
        List<String> metadata = new ArrayList<>();
        for (String field : fields) {
            String type = fieldTypes.get(field);
            metadata.add(field + ":" + type);
        }
        String metadataLine = "#" + String.join(",", metadata);

        try (FileWriter writer = new FileWriter(csvFilePath, false)) {
            // Write the metadata line first
            writer.append(metadataLine).append("\n");

            // Write field names (headers)
            if (!fields.isEmpty()) {
                writer.append(String.join(",", fields)).append("\n");
            }

            // Handle records
            for (Map<String, String> record : inventory.getRecords()) {
                for (int i = 0; i < fields.size(); i++) {
                    String field = fields.get(i);
                    String value = record.getOrDefault(field, "null");
                    writer.append(value != null ? value : "null");
                    if (i < fields.size() - 1) {
                        writer.append(",");
                    }
                }
                writer.append("\n");
            }
            System.out.println("CSV file updated.");
        } catch (IOException e) {
            System.err.println("Error updating CSV: " + e.getMessage());
        }
    }
}
