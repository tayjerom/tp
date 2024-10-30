# Developer Guide

## Acknowledgements
{list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}
* Java Coding Standard (Basic): [Official Documentation](https://se-education.org/guides/conventions/java/basic.html)
* Code Quality (Guide): (https://nus-cs2113-ay2425s1.github.io/website/se-book-adapted/chapters/codeQuality.html)

## Design & implementation
{Describe the design and implementation of the product. Use UML diagrams and short code snippets where applicable.}

Main Components of the architecture

Inventra class is in charge of app launch and shutdown

The majority of the app's work is done by the processes
* UI: The UI of the App
* Command: The commands that user inputs
* CommandParser: A parser to handle said commands
* Inventory: The mode we are operating in
* Storage: Reads and writes data to a csv file

### "Add" Command Feature
The "AddCommand" is responsible for adding fields and records to the inventory. 
Concurrently, "add" command is implemented with behaviour to update CSV file to persist changes made to inventory.
This command supports the following options:
* '-h': Add custom fields to the inventory.
* '-d': Add records to the inventory corresponding to defined fields.
* '-hu': Updates fields in the inventory.

#### Architecture Overview
The 'AddCommand' is part of the `command` package, and interacts with the following key components:
* **Inventory**: Contains the current state of the inventory, including fields and records.
* **Csv**: Handles reading from and writing to the CSV file for data storage persistence.
* **Ui**: Handles user interactions component such as message to user during command execution.

Command flow of 'AddCommand' can be visualized with the following architecture diagram:
![Architecture Diagram](link-to-architecture-diagram)


#### Component-Level Design
"AddCommand" Class:
* processes the input arguments and executes the logic based on flags (-h, -d, -hu) respectively.
* fields and records are validated before updating inventory data into the CSV file.

"AddCommand" Methods:
* handleAddMultipleFields(): processes input fields, validate fields, and update the inventory accordingly.
* handleAddRecord(): handles logic for adding records into the inventory. If successfully add records, data will be appended into the CSV file.
* handleUpdateFields(): allow updating of existing fields and types in the inventory.
  ![Class Diagram](link-to-class-diagram)

#### Sequence Diagram
*Illustrates how "AddCommand" interacts with "Inventory" and "Csv" classes when adding a record:

![Sequence Diagram](link-to-sequence-diagram)



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
![Architecture Diagram](link-to-architecture-diagram)

#### Component-Level Design
"DeleteCommand" Class:
* processes the input arguments and executes the logic based on if the second argument is a number or flag.
* fields and records are validated before updating inventory data into the CSV file.

"DeleteCommand" Methods:
  ![Class Diagram](link-to-class-diagram)

#### Sequence Diagram
*Illustrates how "DeleteCommand" interacts with "Inventory" and "Csv" classes when adding a record:

![Sequence Diagram](link-to-sequence-diagram)

#### Why Implement "DeleteCommand" in this way?
Separating implementation of "DeleteCommand", "Inventory", and "Csv" classes ensures:
* "DeleteCommand" focus only on processing input and delegating task to other specific components.
* "Csv" class is responsible for updating data into CSV file for storage; ensuring persistence in data control.
* With this breakdown of implementation, create room for future scalability (e.g. adding more fields types).

#### Alternative Considered
Only when requirements permits, database implementation has been considered for better handling of fields types for inventory storage.

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
* Run command: "add -h s/name, i/quantity, f/price" to add new fields to inventory
* Verify: "view -a"

2. Adding Records
* Run command: "add -d Apple, 100, 1.5"
* Verify: "view -a"

3. View Records
* To view all records in inventory, run command: "view -a"
* To view specific records in inventory by ID, in this case ID = 1, run command: "view 1"  
* To view records by string, in this case find string "Apple", run command: "view -f Apple"

4. Delete Records
* To delete ID = 1, run command: "delete 1"
* To delete all records, run command: "delete -a"
* To delete entire table (inventory), run command "delete -e"
* Verify: "view -a"

5. CSV Persistence (data storage)
* After add or delete fields/records, check CSV file (inventory.csv) for update

6. Exit Program
* Run command: "exit" 
