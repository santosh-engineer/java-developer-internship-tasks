import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.Scanner;

/**
 * File Handling Utility - Task 1
 * This program demonstrates file operations: read, write, and modify text files
 * 
 * @author Santosh Kumar
 * @version 1.0
 */
public class FileHandlingUtility {
    
    /**
     * Reads content from a text file
     * @param filePath Path to the file to read
     * @return Content of the file as String
     */
    public static String readFile(String filePath) {
        try {
            return Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Writes content to a text file
     * @param filePath Path to the file to write
     * @param content Content to write to the file
     * @return true if successful, false otherwise
     */
    public static boolean writeFile(String filePath, String content) {
        try {
            Files.writeString(Paths.get(filePath), content);
            return true;
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Appends content to an existing file
     * @param filePath Path to the file to append to
     * @param content Content to append
     * @return true if successful, false otherwise
     */
    public static boolean appendToFile(String filePath, String content) {
        try {
            Files.writeString(Paths.get(filePath), content, StandardOpenOption.APPEND);
            return true;
        } catch (IOException e) {
            System.err.println("Error appending to file: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Modifies content in a file by replacing old text with new text
     * @param filePath Path to the file to modify
     * @param oldText Text to replace
     * @param newText Replacement text
     * @return true if successful, false otherwise
     */
    public static boolean modifyFile(String filePath, String oldText, String newText) {
        try {
            String content = Files.readString(Paths.get(filePath));
            String modifiedContent = content.replace(oldText, newText);
            Files.writeString(Paths.get(filePath), modifiedContent);
            return true;
        } catch (IOException e) {
            System.err.println("Error modifying file: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Deletes a file
     * @param filePath Path to the file to delete
     * @return true if successful, false otherwise
     */
    public static boolean deleteFile(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
            return true;
        } catch (IOException e) {
            System.err.println("Error deleting file: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Lists all files in a directory
     * @param directoryPath Path to the directory
     */
    public static void listFiles(String directoryPath) {
        try {
            Files.list(Paths.get(directoryPath))
                 .forEach(System.out::println);
        } catch (IOException e) {
            System.err.println("Error listing files: " + e.getMessage());
        }
    }
    
    /**
     * Demonstration of file operations
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String fileName = "demo.txt";
        
        System.out.println("=== File Handling Utility Demo ===\n");
        
        // 1. Write to file
        System.out.println("1. Writing to file...");
        String initialContent = "Hello, World!\nThis is a demonstration of file handling in Java.\n";
        if (writeFile(fileName, initialContent)) {
            System.out.println("Successfully wrote to " + fileName);
        }
        
        // 2. Read from file
        System.out.println("\n2. Reading from file...");
        String content = readFile(fileName);
        if (content != null) {
            System.out.println("File content:\n" + content);
        }
        
        // 3. Append to file
        System.out.println("3. Appending to file...");
        String appendContent = "This line was appended to the file.\n";
        if (appendToFile(fileName, appendContent)) {
            System.out.println("Successfully appended to " + fileName);
        }
        
        // 4. Read updated content
        System.out.println("\n4. Reading updated content...");
        content = readFile(fileName);
        if (content != null) {
            System.out.println("Updated file content:\n" + content);
        }
        
        // 5. Modify file content
        System.out.println("5. Modifying file content...");
        if (modifyFile(fileName, "Hello, World!", "Hello, Java!")) {
            System.out.println("Successfully modified " + fileName);
        }
        
        // 6. Read modified content
        System.out.println("\n6. Reading modified content...");
        content = readFile(fileName);
        if (content != null) {
            System.out.println("Modified file content:\n" + content);
        }
        
        // 7. Interactive mode
        System.out.println("\n7. Interactive Mode:");
        System.out.print("Enter text to append to file: ");
        String userInput = scanner.nextLine();
        if (appendToFile(fileName, userInput + "\n")) {
            System.out.println("Your text has been added to the file!");
        }
        
        // 8. Final file content
        System.out.println("\n8. Final file content:");
        content = readFile(fileName);
        if (content != null) {
            System.out.println(content);
        }
        
        // 9. Clean up (optional)
        System.out.print("\nDo you want to delete the demo file? (y/n): ");
        String choice = scanner.nextLine();
        if (choice.toLowerCase().startsWith("y")) {
            if (deleteFile(fileName)) {
                System.out.println("Demo file deleted successfully!");
            }
        }
        
        scanner.close();
        System.out.println("\nFile handling demonstration completed!");
    }
}