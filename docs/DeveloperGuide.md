# Developer Guide

## Acknowledgements
The following resources and libraries were referenced or adapted in this project:
* Java Coding Standard (Basic): [Official Documentation](https://se-education.org/guides/conventions/java/basic.html)
* Code Quality (Guide): (https://nus-cs2113-ay2425s1.github.io/website/se-book-adapted/chapters/codeQuality.html)

## Design & implementation

### Main Components of the architecture

The `Inventra` class manages the overall application flow, which includes application launch and shutdown.
The core processes are managed by the following components:
* **UI**: The user interface for handling user interaction and the display of output.
* **Command**: The commands that user inputs.
* **CommandParser**: A parser to handle said commands, as well as interpreting flags and arguments.
* **Inventory**: The mode we are operating in.
* **Storage**: Reads and writes data to a CSV file to maintain data persistence.

### "Add" Command Feature
The "AddCommand" is responsible for adding fields and records to the inventory. 
Concurrently, "add" command is implemented with behaviour to update CSV file to persist changes made to inventory.

This command supports the following options:
* '-h': Add custom fields to the inventory.
* '-d': Add records to the inventory corresponding to defined fields.

#### Architecture Overview
The 'AddCommand' is part of the `command` package, and interacts with the following key components:
* **Inventory**: Contains the current state of the inventory, including fields and records.
* **Csv**: Handles reading from and writing to the CSV file for data storage persistence.
* **Ui**: Handles user interactions component such as message to user during command execution.

Command flow of 'AddCommand' can be visualized with the following architecture diagram:
![Architecture Diagram](docs/diagrams/AddCommandArchitectureDiagram.png)

* **Description**: The `AddCommand` class modifies the `Inventory` by adding new fields or records based on user's inputs.
It interacts with `Csv` to update the CSV file accordingly to maintain data persistence. 
The `Ui` components then displays feedback to the user.

#### Component-Level Design
"AddCommand" Class:
* processes the input arguments and executes the logic based on flags (-h, -d) respectively.
* fields and records are validated before updating inventory data into the CSV file.

"AddCommand" Methods:
* handleAddMultipleFields(): processes input fields, validate fields, and update the inventory accordingly.
* handleAddRecord(): handles logic for adding records into the inventory. If successfully add records, data will be appended into the CSV file.
* handleUpdateFields(): allow updating of existing fields and types in the inventory.
  ![Class Diagram](docs/umldiagrams/AddCommandClassDiagram.png)
* **Description**: The class diagram illustrates methods of the `AddCommand` class and the interaction with `Inventory`, `Csv`, and `Ui` respectively.

#### Sequence Diagram
*Illustrates how "AddCommand" interacts with "Inventory" and "Csv" classes when adding a record:

![Sequence Diagram](docs/diagrams/AddCommandSequenceDiagram.png)

#### Why Implement "AddCommand" in this way?
Separating implementation of "AddCommand", "Inventory", and "Csv" classes ensures:
* "AddCommand" focus only on processing input and delegating task to other specific components.
* "Csv" class is responsible for updating data into CSV file for storage; ensuring persistence in data control.
* With this breakdown of implementation, create room for future scalability (e.g. adding more fields types).

#### Alternative Considered
Only when requirements permits, database implementation has been considered for better handling of fields types for inventory storage.

### "Delete" Command Feature
The "DeleteCommand" is responsible for adding fields and records to the inventory. 
Concurrently, "delete" command is implemented with behaviour to update CSV file to persist changes made to inventory.
This command supports the following formats:
* '\<index>': Delete the record at the specified index (1-based indexing).
* '-a': Delete all records in the inventory. 
* '-e': Delete all records and headers in the inventory.
* '-h \<field_name>': Delete the mentioned field and its column from the inventory.
* '-r \<start>-\<end>': Delete records from the start index to the end index (both inclusive and 1-based indexing).

#### Architecture Overview
The 'DeleteCommand' is part of the `command` package, and interacts with the following key components:
* **Inventory**: Contains the current state of the inventory, including fields and records.
* **Csv**: Handles reading from and writing to the CSV file for data storage persistence.
* **Ui**: Handles user interactions component such as message to user during command execution.

Command flow of 'DeleteCommand' can be visualized with the following architecture diagram:
![Architecture Diagram](docs/diagrams/DeleteCommandArchitectureDiagram.png)
* **Description**: The `DeleteCommand` modifies `Inventory` by removing fields or records that are specified by the user.
The `Csv` components updates the CSV file to reflect changes made during deletion, as well as `Ui` provide response to the user.

#### Component-Level Design
"DeleteCommand" Class:
* processes the input arguments and executes the logic based on if the second argument is a number or flag.
* fields and records are validated before updating inventory data into the CSV file.

"Delete Command" key methods include:
* `deleteSingleRecord()`: Deletes a specific record by defined index.
* `deleteAllRecords()`: Deletes all records from the inventory.
* `deleteHeaderAndColumn()`: Deletes a specific field.
* `deleteRangeRecords()`: Deletes a range of records. 

![Class Diagram](docs/diagrams/DeleteCommandClassDiagram.png)
* **Description**: The class diagram above shows the core methods of `DeleteCommand` and its interactions with `Inventory`, `Csv`, and `Ui`.

#### Sequence Diagram
*Illustrates how "DeleteCommand" interacts with "Inventory" and "Csv" classes when deleting a record:

![Sequence Diagram](docs/diagrams/DeleteCommandSequenceDiagram.png)

#### Why Implement "DeleteCommand" in this way?
Separating implementation of "DeleteCommand", "Inventory", and "Csv" classes ensures:
* "DeleteCommand" focus only on processing input and delegating task to other specific components.
* "Csv" class is responsible for updating data into CSV file for storage; ensuring persistence in data control.
* With this breakdown of implementation, create room for future scalability (e.g. adding more fields types).

#### Alternative Considered
As with `AddCommand`, database implementation has been considered for better handling of fields types for inventory storage.


### "View" Command Feature
The `ViewCommand` allows users to view records in the inventory, either displaying all records or specific ones based on user's defined filters.

This command supports the following formats:
* `-a`: View all records.
* `<ID>`: View a specific record by defined ID.
* `-f <keyword>`: View records containing a keyword.

#### Architecture Overview

The `ViewCommand` interacts with:
* **Inventory**: Accessing data to retrieve and display records
* **Ui**: Displays records to the user.

![ViewCommand Architecture Diagram](docs/diagrams/ViewCommandArchitectureDiagram.png)
* **Description**: The `ViewCommand` retrieve data from `Inventory` based on user's input and leverage on `Ui` to display records.

#### Component-Level Design

The `ViewCommand` class processes input arguments and execute respective logic based on flags or IDs provided.

"View Command" key methods include:
* `handleViewById()`: Displays a specific record by ID
* `handleViewByKeyword()`: Filters and displays records based on defined keyword.

![ViewCommand Class Diagram](docs/diagrams/ViewCommandClassDiagram.png)
* **Description**: The class diagram above shows the main methods used by `ViewCommand` and its connection to `Inventory` and `Ui`.

#### Sequence Diagram
The following sequence diagram shows how `ViewCommand` interacts with `Inventory` and `Ui` when displaying a specific records:

![ViewCommand Sequence Diagram](docs/diagrams/ViewCommandSequenceDiagram.png)

#### Why Implement `ViewCommand` in this way:
Approach was adopted to ensure efficient access to `Inventory` for data retrieval and provide overall user-friendliness for user viewing records through `Ui`.


## Product scope
### Target user profile
* Small to medium-sized business owners operating as retail stores, warehouses, or online shops.
* Owners seeking a customizable, command-driven system to efficiently manage inventory and optimize workflows can leverage on Inventra.

### Value proposition
Inventra provides a fast, command-line driven logistics and inventory management system, allowing a single user to do stock tracking, order management, and operational insights, ensuring quick and snappy access to critical business data.

## User Stories

| Version | As a ...       | I want to ...                                     | So that ...                                                                      |
|--------|----------------|---------------------------------------------------|----------------------------------------------------------------------------------|
| v1.0   | business owner | add new products to the inventory                 | I can keep track of stock availability.                                          |
| v1.0   | business owner | delete discontinued products from inventory       | the inventory can be up-to-date on the existing products.                        |
| v1.0   | business owner | view current inventory list                       | I can efficiently access in-stock products and to handle restocking of products. |
| v1.0   | business owner | import and export inventory data into spreadsheet | I can streamline inventory updates.                                              |
| v1.0   | business owner | customize information type related to products    | I can have the flexibility to manage inventory better.                           |
| v2.0   | business owner | search for products in the inventory              | I can find specific items quickly.                                               |
| v2.0   | business owner | delete all products in inventory                  | I can handle incorrect creation of inventory data.                               |
| v2.0   | business owner | update product details                            | I make adjustments to the inventory when required.                               |

## Non-Functional Requirements
* The system should work across different operating systems (Windows, Linux, macOS).
* Data must be persistent across sessions by saving and fetching from/to a CSV file.

## Glossary

* *CSV* - Comma-separated values, a format used to store data in a table that are separated by commas
* *Inventory* - Collections of items defined based on their associated properties (fields) - for example: product name, quantity, ...).

## Instructions for manual testing
1. Adding Fields
* Run command: `add -h s/name, i/quantity, f/price` to add new fields to inventory
* Verification: `view -a`

2. Adding Records
* Run command: `add -d Apple, 100, 1.5`
* Verification: `view -a`

3. View Records
* To view all records in inventory, run command: `view -a`
* To view specific records in inventory by ID, in this case ID = 1, run command: `view 1`  
* To view records by string, in this case find string "Apple", run command: `view -f Apple`

4. Delete Records
* To delete ID = 1, run command: `delete 1`
* To delete all records, run command: `delete -a`
* To delete entire table (inventory), run command `delete -e`
* Verification: `view -a`

5. CSV Persistence (data storage)
* After add or delete fields/records, check CSV file (`inventory.csv`) for update

6. Exit Program
* Run command: `exit` 
