package seedu.ui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Concat {

    // Set this relative path to match your project structure
    private static final String SOURCE_DIRECTORY = "src/main/java/seedu";
    private static final String OUTPUT_FILE = "allJavaCode.txt";

    public static void main(String[] args) {
        System.out.println("Checking directory: " + SOURCE_DIRECTORY); // Debugging output
        File directory = new File(SOURCE_DIRECTORY);

        if (!directory.exists()) {
            System.out.println("Directory does not exist: " + directory.getAbsolutePath());
            return;
        } else if (!directory.isDirectory()) {
            System.out.println("Not a directory: " + directory.getAbsolutePath());
            return;
        }

        try (FileWriter writer = new FileWriter(OUTPUT_FILE, true)) {
            appendJavaFiles(directory, writer);
            System.out.println("All Java code has been appended to " + OUTPUT_FILE);
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    private static void appendJavaFiles(File dir, FileWriter writer) throws IOException {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    appendJavaFiles(file, writer); // Recursively search subdirectories
                } else if (file.isFile() && file.getName().endsWith(".java")) {
                    appendFileContent(file, writer);
                }
            }
        }
    }

    private static void appendFileContent(File file, FileWriter writer) throws IOException {
        String filePath = file.getPath().replace("\\", "/").replace(SOURCE_DIRECTORY, "java");
        writer.write("[" + filePath + "]:\n\n");

        List<String> lines = Files.readAllLines(Paths.get(file.getPath()));
        for (String line : lines) {
            writer.write(line + "\n");
        }
        writer.write("\n\n"); // Add space between files
    }
}
