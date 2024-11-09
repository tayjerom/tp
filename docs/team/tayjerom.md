# Jerome Tay - A0253346E - (tayjerom) - Project Portfolio Page (PPP)

## Project Overview: Inventra  
Inventra is a CLI-based inventory management system designed for small and medium-sized businesses.
It provides users with tools to manage custom inventory fields, track records and generate insights.
This product was build with the contribution of T11 G4 team members, as part of CS2113 module.

## Summary of Contributions  
### Code Contributions:
Please view all my contributions to the project codes [here](https://nus-cs2113-ay2425s1.github.io/tp-dashboard/?search=tayjerom&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2024-09-20&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other).  

### Enhancements Implemented:
1. Implemented Core Functionalities:
* `add -d`: Enable user to add records based on user-defined custom fields.
* `view <id>`: Implemented `view <ID>` and `view -a` to enable different viewing modes for user.
* `add -l`: Feature was debunked and merged into `view` command.
* `add -hu`: Feature was removed and merged into `update` command.
* Implemented `CSV integration` to enable data storage persistence, which includes:
    - CSV export and import functionalities
    - Debugged CSV handling issue by adding meta-header row to ensure data persistence for field "type".  

2. Exception Handling and Assertions:
* Custom exception were build on-top of existing code to handle invalid inputs:
    - InventraInvalidTypeException
    - InventraExcessArgsException
    - InventraMissingFieldsException
* Implemented assertions for defensive programming
* Additionally, perform regular Gradle test and CI Pipelines, which includes:
  - performed checks to ensure program's sanity
  - ensured program is free from any checkstyle errors: improper naming convention, line length violation, and unused imports
  - addressed bugs highlighted by peers in PE-D  

### Contributions to User Guide (UG)
1. Contributed and Updated the following `Command` Sections: `add`, `view`, `exit`
   - Detailed annotation on individual command's: General format, flags, and command examples.
   - Re-formatted `update` command section to match the standards of UG documentation.

2. Additional work done:
* Enhanced formatting consistency across the UG.
* Upload and revised v1.0 UG to match the current project progress, which includes sections:
  - Introduction, Quick Start, Features, FAQ, Known Issues, and Command Summary
* Consistent update to UG to ensure it is up-to-date with the implementation of Inventra:
  - Bug fixes in documentation (i.e. removing deprecated commands like `add -l` from UG).
  - Updated sections on invalid input handling.

### Contributions to Developer Guide (DG)
* Provided detailed implementation descriptions for `AddCommand`, `ViewCommand`, `DeleteCommand`, and `Csv`:
    - Included method breakdown and descriptions of class, key methods and flags involved.
    - Documented rationale of method-used to implement `AddCommand`, and alternative considerations.
* Formatting and Structure:
    - Documented Acknowledgements section
    - Included the following subsections:
      - Product Scope
      - Target User Profile
      - Value Proposition
      - (Updated) User Stories
      - Non-Functional Requirements (NFR)
      - Glossary
      - Instructions for Manual Testing  

### Contributions to Team-Based Tasks
1. Milestone Management:
* Setup Team's Organization GitHub repository, consisting of:
    - Created `team`, invited and allocate access-rights to team members
    - Setup of tP Issue Tracker
    - Managed GitHub milestones details for v2.0 and v2.1
* Updated master/README.md to reflect `Inventra's` product description and links to respective technical guides (UG & DG).

2. Project Management:
* Coordination of project contributors which include:
    - Establishing a common document to document member's inputs and opinions (GoogleDocs, Telegram Chat)
    - Initializing project meetings and contributed in elements of time management.
  
### Summary
Through my contributions to `Inventra`, I have implemented several core functionalities, 
improved user experience (CLI-based), as well as ensured proper exception handling and data persistence.
I have contributed my share in improving project documentation, and ensure that the team is 
on track with assigned deliverables to be completed and deadline to adhere to.
