# Inventra

Inventra is a command-line, greenfield Java project designed for small to medium-sized businesses. It provides robust tools for inventory and logistics management, enabling business owners to track stock, manage orders, and gain insights into operational efficiencies.

Below are instructions on setting up and using Inventra:

## Setting up in Intellij

* Prerequisites: JDK 17 (use this exact version).
* IntelliJ IDEA, updated to the most recent version.

1. **Ensure Intellij JDK 17 is defined as an SDK**, as described [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk) -- this step is not needed if you have used JDK 17 in a previous Intellij project.
1. **Import the project _as a Gradle project_**, as described [here](https://se-education.org/guides/tutorials/intellijImportGradleProject.html).
1. **Verify the setup**: After the importing is complete, locate the `src/main/java/seedu/inventra/Inventra.java` file, right-click it, and choose `Run Inventra.main()`. If the setup is correct, you should see:
   ```
   > Task :compileJava
   > Task :processResources NO-SOURCE
   > Task :classes
   
   > Task :seedu.inventra.Inventra.main()
   Finished loading CSV file.
   Welcome to
    ___ _   ___     _______ _   _ _____ ____      _    
   |_ _| \ | \ \   / / ____| \ | |_   _|  _ \    / \   
    | ||  \| |\ \ / /|  _| |  \| | | | | |_) |  / _ \  
    | || |\  | \ V / | |___| |\  | | | |  _ <  / ___ \ 
   |___|_| \_|  \_/  |_____|_| \_| |_| |_| \_\/_/   \_\

   Type help to receive manual.
   ```
   Type 'help' word and press enter to see Inventra's manual.

## Build automation using Gradle

* This project uses Gradle for build automation and dependency management. It includes a basic build script as well (i.e. the `build.gradle` file).
* If you are new to Gradle, refer to the [Gradle Tutorial at se-education.org/guides](https://se-education.org/guides/tutorials/gradle.html).

## Testing

### I/O redirection tests

* To run _I/O redirection_ tests (aka _Text UI tests_), navigate to the `text-ui-test` and run the `runtest(.bat/.sh)` script.

### JUnit tests

* A skeleton JUnit test (`src/test/java/seedu/inventra.java`) is provided with this project template. 
* If you are new to JUnit, refer to the [JUnit Tutorial at se-education.org/guides](https://se-education.org/guides/tutorials/junit.html).

## Checkstyle

* A sample CheckStyle rule configuration is provided in this project.
* If you are new to Checkstyle, refer to the [Checkstyle Tutorial at se-education.org/guides](https://se-education.org/guides/tutorials/checkstyle.html).

## Continuous Integration using GitHub Actions

Inventra uses GitHub Actions for Continuous Integration (CI). Whenever you push commits or create a pull request (PR), [GitHub actions](https://github.com/features/actions) will automatically build the project and run tests to ensure that everything is working as expected.

## Documentation

`/docs` folder contains the Inventra User Guide, Developer Guide, and Developer's information (for your reference).

## Key Features of Inventra:
1. Custom Field Management: Create and manage fields dynamically based on user's own customization (e.g. name, quantity, price).
2. Inventory Tracking: A real-time, resilient system designed to ensure seamless management of items in your inventory.
3. Storage persistency: provides continuous and reliable inventory tracking, even in the face of unexpected disruptions.
4. Command-Driven Interface: Execute commands for quick, efficient operations.

