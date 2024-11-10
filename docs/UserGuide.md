# Inventra User Guide

[View on GitHub](https://github.com/AY2425S1-CS2113-T11-4/tp)

# Table of Contents

- [Introduction](#introduction)
- [Quick Start](#quick-start)
- [Features](#features)
    - [Getting Help `help`](#getting-help-help)
        - [Display Entire Help Page](#display-entire-help-page)
        - [Display Help Page For Specific Command](#display-help-page-for-specific-command)
    - [Adding New Fields and Records: `add`](#adding-new-fields-and-records-add)
        - [Add Fields](#add-fields)
        - [Add Records](#add-records)
    - [Viewing Records: `view`](#viewing-records-view)
        - [View All Records](#view-all-records)
        - [View a Specific Record by ID](#view-a-specific-record-by-id)
    - [Updating Fields and Records: `update`](#updating-fields-and-records-update)
        - [Update Fields](#update-fields)
        - [Update Records](#update-records)
    - [Deleting Fields and Records: `delete`](#deleting-fields-and-records-delete)
        - [Delete All Fields and Records](#delete-all-fields-and-records)
        - [Delete All Records](#delete-all-records)
        - [Delete Specific Field](#delete-specific-field)
        - [Delete Specific Record](#delete-specific-record)
        - [Delete Range of Records](#delete-range-of-records)
    - [Exiting the Program: `exit`](#exiting-the-program-exit)
- [FAQ](#faq)
- [Known Issues](#known-issues)
- [Command Summary](#command-summary)


## Introduction

Inventra is a command-driven logistics and inventory management system design for small to
medium-sized businesses including but not limited to: warehouses, online shops, retail
businesses. Optimized for usage via Command Line Interface (CLI), Inventra aims to provide
precise inventory management, real-time tracking and automation of tasks.

## Quick Start

1. Prerequisites: Ensure Java 17 or above is installed in your Computer.
2. Download: the latest [.jar] file from this [link](https://github.com/AY2425S1-CS2113-T11-4/tp/releases/tag/v2.0).
3. Setup:
*  Copy [.jar] file to the folder you would like to store your program to. 
*  Open command terminal, [cd] into directory [.jar] is stored
4. Run the application:
   ``` java -jar inventra.jar ```
5. Getting Started:
*  Once launched the program, type `help` for user manual will be displayed. 
Refer to the Features below for details of each command.

## Features 

Notes about commands' convention:
1. Program function keywords are `help`, `add`, `view`, `delete` , `update` and `exit`
2. Most key words will require flags via a dash, an alphabet followed by valid input
   e.g view [FLAG] [VALID INPUT]
3. Some key words won't require flags: help, delete 1, exit
3. Words in UPPER_CASE are parameters to be supplied by the user.
   e.g in add -h FIELD1, FIELD2…
4. Items with “...” after them can be used multiple times
   e.g in add -d DATA1, DATA2…
5. Any extra parameters defined after the following commands: view -a, delete -a, delete -e, exit will be
   ignored.
6. All inputs to inventory is limited to 20 characters, this is to save space and preserve table Ui

### Getting Help `help`
Get help from manual page for inventra

#### Display Entire Help Page
* Command: `help`

#### Display Help Page For Specific Command
* Command: `help COMMAND`
* Example: `help delete`
* Note: COMMAND works for specific command, excluding `help` command itself!

### Adding New Fields and Records: `add`
Add new fields or records to the inventory.

General format: add [FLAG] [VALID INPUT]

#### Add Custom Fields with specific types: `add -h`
* Command: `add -h TYPE/FIELD1, TYPE/FIELD2, ...`
    * Types: Supported data types:
        * `s`: String
        * `i`: Integer
        * `f`: Float
        * `d`: Date
    * Example:
    ``` add -h s/name, i/quantity, f/price, d/expiry ```

#### Add Records to custom fields created: `add -d`
* Command: `add -d VALUE1, VALUE2, ...`
    * Note:
        - The order of values should match fields defined using `add -h`.
        - **Supported date formats:** `DD/MM/YYYY` or `DD/MM/YY`.
    * Example:
    ``` 
    add -d Apple, 100, 1.50, 01/10/2024 
    ```

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

#### View Records related to Keyword
* Command: `view -f KEYWORD`
    * Display records that contains the keyword
    * Example: ``` view -f apple ```
      
### Updating Fields and Records: `update`
Update field or record in the inventory.

General format: update [FLAG] [VALID INPUT]


#### Update Field
* Command: `update -h OLDFIELD, NEWFIELD`
    * Constraints:
      - Current field name must exist in the inventory.
    * Example:
      ``` update -h name, product_name ```

#### Update Record
* Note: The new value can only be of the previously declared data type of the field
* Command: `update -d ID, FIELD, NEWVALUE`
    * Constraints:
      - `ID` must be valued (between 1 and total number of records).
      - `FIELD` must exist
      - `NEWVALUE` must adhere to predefined data type of the field
    * Example:
      ``` update -d 1, price, 1.50 ```

### Deleting Fields and Records: `delete`
Delete all or specific fields or records

General Format: `delete [FLAG] [INPUT]`

#### Delete All Fields and Records
* Command: `delete -e`
    * Note: Will irretrievably remove all fields and records BE CAREFUL!

#### Delete All Records
* Command: `delete -a`
    * Will remove all records but preserve same fields
    * Note: Will irretrievably remove all records BE CAREFUL!

#### Delete Specific Field
* Command: `delete -h FIELD`
    * Example: delete -h quantity
    * Deletes the entire column from the inventory based on mentioned field
    * 
#### Delete Specific Record
* Command: `delete RECORD_ID`
* Delete a single record based on specific ID (1-based indexing).
    * Example: delete 1
  
#### Delete Range of Records
* Command: `delete -r STARTID-ENDID`
* Deletes records from the start index to the end index (both inclusive and 1-based indexing).
    * Example: delete -r 1-5

### Exiting the Program: `exit`
Terminates the program.  
    * Command: `exit`  
        * Exit Inventra.  
        * Example:
        ``` exit ```

## FAQ

**Q**: How do I transfer my data to another computer?  
**A**: Copy `data/inventory.csv` file to its new desired location.
Ensure that the file remains in the same directory as `inventra.jar` file when running the application on the new computer.  

**Q**: Why can't I input more than 20 characters when adding a field or record?  
**A**: This is to ensure efficient use of space and to ensure that output table format is preserved.  

## Known Issues

1. Extra Input: Additional values provided after expected inputs for commands like `view -a`, `exit` will be ignored.
2. Case Sensitivity: Ensure correct lowercase input as commands are case-sensitive.
3. Pending Features: Increasing limitations of 20 character fields and records with dynamic output table Ui.

## Command Summary

| Action                   | Format & Example                                        |
|--------------------------|---------------------------------------------------------|
| **Get Help**             | `help` / `help [COMMAND]`                               |
|                          | Example: `help` / `help delete`                         |
| **Add Field**            | `add -h TYPE/FIELD1, TYPE/FIELD2,...`                   |
|                          | Example: `add -h s/name, i/quantity, f/price, d/expiry` |
| **Add Record**           | `add -d VALUE1, VALUE2,...`                             |
|                          | Example: `add -d Apple, 100, 1.50, 01/10/2024`          |
| **View All Records**     | `view -a`                                               |
|                          | Example: `view -a`                                      |
| **View Specific Record** | `view RECORD_ID`                                        |
|                          | Example: `view 1`                                       |
| **Update Field**         | `update -h OLDFIELD, NEWFIELD`                          |
|                          | Example: `update -h name, product_name`                 |
| **Update Record**        | `update -d RECORD_ID, FIELD, NEWVALUE`                  |
|                          | Example: `update -d 1, price, 1.50`                     |
| **Delete All Fields**    | `delete -e`                                             |
|                          | Example: `delete -e`                                    |
| **Delete All Records**   | `delete -a`                                             |
|                          | Example: `delete -a`                                    |
| **Delete Field**         | `delete -h FIELD_NAME`                                  |
|                          | Example: `delete -h quantity`                           |
| **Delete Specific Record**| `delete RECORD_ID`                                      |
|                          | Example: `delete 1`                                     |
| **Delete Range of Records**| `delete -r STARTID-ENDID`                               |
|                          | Example: `delete -r 1-5`                                |
| **Exit Program**         | `exit`                                                  |
|                          | Example: `exit`                                         |

