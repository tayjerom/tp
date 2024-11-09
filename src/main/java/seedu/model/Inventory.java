package seedu.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory {
    private List<String> fields;
    private Map<String, String> fieldTypes; // Map field <-> type
    private List<Map<String, String>> records; // List of mapped field <-> record


    public Inventory() {
        this.fields = new ArrayList<>();
        this.fieldTypes = new HashMap<>();
        this.records = new ArrayList<>();
    }

    public Inventory(List<String> fields, Map<String, String> fieldTypes, List<Map<String, String>> records) {
        this.fields = fields;
        this.fieldTypes = fieldTypes;
        this.records = records;
    }

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

    public void setRecords(List<Map<String, String>> records) {
        this.records = records;
    }

    // Method to set fields (for loading from CSV)
    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public void setFieldTypes(Map<String, String> newFieldTypes) {
        this.fieldTypes = newFieldTypes;
    }

    public boolean isStringField(String fieldName) {
        return "s".equals(fieldTypes.get(fieldName));  // "s" denotes a String type field
    }
}
