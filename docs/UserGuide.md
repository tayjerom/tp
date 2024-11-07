# User Guide

## Introduction

Inventra is a command-driven logistics and inventory management system design for small to
medium-sized businesses including but not limited to: warehouses, online shops, retail
businesses. Optimized for usage via Command Line Interface (CLI), Inventra aims to provide
precise inventory management, real-time tracking and automation of tasks.

## Quick Start

1. Prerequisites: Ensure Java 17 or above is installed in your Computer.
2. Download: the latest [.jar] file from this link.
3. Setup:
*  Copy [.jar] file to the folder you would like to store your program to. 
*  Open command terminal, [cd] into directory [.jar] is stored, use the following command to
4. Run the application:
   ``` java -jar inventra.jar ```
5. Getting Started:
*  Once launched the program, the user manual will be displayed for ease of use. 
Refer to the Features below for details of each command.

## Features 

Notes about commands' convention:
1. **Program function keywords are `add`, `view`, `delete` and `exit`
2. Flags defined for each function via a dash, an alphabet followed by valid input
   e.g view [FLAG] [VALID INPUT}
3. Items in square brackets are optional. But using keywords by themselves would print
   out the man page for the particular function. e.g add => add man page
4. Words in UPPER_CASE are parameters to be supplied by the user.
   e.g in add -h FIELD1, FIELD2…
5. Items with “...” after them can be used multiple times
   e.g in add -d DATA1, DATA2…
6. Any extra parameters defined after the following commands: add -l, view -a, exit will be
   ignored.

### Adding New Fields and Records: `add`
Add new fields or records to the inventory.

General format: add [FLAG] [VALID INPUT]

#### Add Fields
* Command: `add -h TYPE/FIELD1, TYPE/FIELD2, ...`
    * Types: Supported data types:
        * `s`: String
        * `i`: Integer
        * `f`: Float
        * `d`: Date
    * Example:
    ``` add -h s/name, i/quantity, f/price, d/expiry ```

#### Add Records
* Command: `add -d VALUE1, VALUE2, ...`
    * Note: The orders of values should match fields defined using `add -h`
    * Example:
    ``` add -d Apple, 100, 1.50, 01/10/2024 ```

#### List Fields added
* Command: `add -l`
    * Displays all currently defined fields names 
    * Example:
    ``` add -l ```

### Viewing Records: `view`
Displays all or specific records

General Format: `view [FLAG] [INPUT]`

#### View All Records
* Command: `view -a`
    * List all items in the inventory.
    * Example:
    ``` view -a ```

#### View a Specific Record by ID
* Command: `view RECORD_ID`
    * Display details for the item with the specific ID.
    * Example:
    ``` view 1 ```

#### View Records in a Range 
* Command: `view -r START-END`
    * Views records in a specified range.
    * Example:
    ``` view -r 1-3 ```

### Deleting a record : `delete`
Deletes a record from the inventory.

Format: 
- `delete <index>`
- `delete -a`
- `delete -e`
- `delete -h <field_name>`
- `delete -r <start>-<end>`

* `delete <index>` deletes the record at the specified index (1-based indexing).
* `delete -a` deletes all records in the inventory.
* `delete -e` deletes all records and headers in the inventory.
* `delete -h <field_name>` deletes the mentioned field and its column from the inventory.
* `delete -r <start>-<end>` deletes records from the start index to the end index (both inclusive and 1-based indexing).

Example of usage: 

- `delete 1`
- `delete -a`
- `delete -e`
- `delete -h price`
- `delete -r 1-5`


### Update Fields and Records: `update`
Modify field names or update specific records in the inventory.

General Format: `update [FLAG] [VALID INPUT]`

#### Update Field Names
* Command: `update -h CURRENT_FIELD_NAME, NEW_FIELD_NAME`
    * Updates the name of an existing field to new field name.
    * Constraints:
      - Both fields names must be non-empty and no longer than 20 characters.
      - Current field name must exist in the inventory.
    * Example:
    ``` update -h name, product_name ```

#### Update Record Values
* Command: `update -h RECORD_ID, FIELD_NAME, NEW_VALUE`
    * Updates a specific value in a record for given field.
    * Constraints:
      - `RECORD_ID` must be valud (between 1 and total number of records).
      - `FIELD_NAME` must exist
      - `NEW_VALUE` must adhere to predefined data type of the field
    * Example:
    ``` update -d 3, quantity, 150 ```

### Exiting the Program: `exit`
Closes the program.
    * Command: `exit`
        * Exit Inventra.
        * Example:
        ``` exit ```

## FAQ

**Q**: How do I transfer my data to another computer?
**A**: Copy `data/inventory.csv` file to its new desired location. 
Ensure that the file remains in the same directory as `inventra.jar` file when running the application on the new computer.


## Known Issues

1. Extra Input: Additional values provided after expected inputs for commands like `add -l`, `view -a`, `exit` will be ignored.
2. Case Sensitivity: Ensure correct lowercase input as commands are case-sensitive.
3. Pending Features: ...


## Command Summary

| Action               | Format & Example                               |
|----------------------|------------------------------------------------|
| **Add Field**        | `add -h TYPE/FIELD1, TYPE/FIELD2,...`          |
|                      | Example: `add -h s/name, i/quantity, d/expiry` |
| **Add Record**       | `add -d VALUE1, VALUE2,...`                    |
|                      | Example: `add -d Apple, 100, 01/10/2024`       |
| **List Fields**      | `add -l`                                       |
|                      | Example: `add -l`                              |
| **View All Records** | `view -a`                                      |
|                      | Example: `view -a`                             |
| **View Specific**    | `view RECORD_ID`                               |
|                      | Example: `view 1`                              |
| **View Range**       | `view -r START-END`                            |
|                      | Example: `view -r 1-3`                         |
| **Delete Specific**  | `delete RECORD_ID`                             |
|                      | Example: `delete 1`                            |
| **Delete All**       | `delete -a`                                    |
|                      | Example: `delete -a`                           |
| **Delete Table**     | `delete -e`                                    |
|                      | Example: `delete -e`                           |
| **Delete Field**     | `delete -h FIELD_NAME`                         |
|                      | Example: `delete -h price`                     |
| **Delete Range**     | `delete -r START-END`                          |
|                      | Example: `delete -r 1-5`                       |
| **Update Field**     | `update -h CURRENT_FIELD_NAME, NEW_FIELD_NAME` |
|                      | Example: `update -h quantity, number of items` |
| **Update Record**    | `update -d RECORD_ID, FIELD_NAME, NEW_VALUE`   |
|                      | Example:     `update -d 4, price, 200.50`       |
| **Exit**             | `exit`                                         |
|                      | Example: `exit`                                |
