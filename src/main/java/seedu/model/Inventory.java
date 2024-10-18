package seedu.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory {
    private List<String> fields = new ArrayList<>();
    private Map<String, String> fieldTypes = new HashMap<>(); // Map field <-> type
    private final List<Map<String, String>> records = new ArrayList<>(); // List of mapped field <-> record

    public void addField(String field, String type) {
        fields.add(field);
        fieldTypes.put(field, type);
    }

    public List<String> getFields() {
        return fields;
    }

    public Map<String, String> getFieldTypes() {
        return fieldTypes;
    }

    public void addRecord(Map<String, String> record) {
        records.add(record);
    }

    public List<Map<String, String>> getRecords() {
        return records;
    }

    // Method to set fields (for loading from CSV)
    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public void setFieldTypes(Map<String, String> newFieldTypes) {
        this.fieldTypes = newFieldTypes;
    }
}
