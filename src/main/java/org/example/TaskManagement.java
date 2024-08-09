package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TaskManagement {
    private static final String FILE_PATH = "C:\\Users\\jeffrey.samuel\\IdeaProjects\\Json\\tasks.json";
    private static ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private static List<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        loadTasks();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Add a New Task");
            System.out.println("2. View All Tasks");
            System.out.println("3. Update Task Details");
            System.out.println("4. Delete a Task");
            System.out.println("5. Mark Task as Completed");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> addTask(scanner);
                case 2 -> viewTasks();
                case 3 -> updateTask(scanner);
                case 4 -> deleteTask(scanner);
                case 5 -> markTaskAsCompleted(scanner);
                case 6 -> {
                    saveTasks();
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void loadTasks() {
        try {
            File file = new File(FILE_PATH);
            if (file.exists()) {
                tasks = mapper.readValue(file, new TypeReference<List<Task>>() {});
            }
        } catch (IOException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }
    }

    private static void saveTasks() {
        try {
            mapper.writeValue(new File(FILE_PATH), tasks);
            System.out.println("Tasks saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    private static void addTask(Scanner scanner) {
        Task task = new Task();
        System.out.print("Enter Task ID: ");
        task.setId(scanner.nextInt());
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Description: ");
        task.setDescription(scanner.nextLine());
        System.out.print("Enter Due Date: ");
        task.setDueDate(scanner.nextLine());
        task.setCompleted(false);
        tasks.add(task);
        saveTasks();
    }

    private static void viewTasks() {
        for (Task task : tasks) {
            System.out.println("ID: " + task.getId());
            System.out.println("Description: " + task.getDescription());
            System.out.println("Due Date: " + task.getDueDate());
            System.out.println("Completed: " + task.isCompleted());
            System.out.println();
        }
    }

    private static void updateTask(Scanner scanner) {
        System.out.print("Enter Task ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        for (Task task : tasks) {
            if (task.getId() == id) {
                System.out.print("Enter new Description: ");
                task.setDescription(scanner.nextLine());
                System.out.print("Enter new Due Date: ");
                task.setDueDate(scanner.nextLine());
                saveTasks();
                return;
            }
        }
        System.out.println("Task not found.");
    }

    private static void deleteTask(Scanner scanner) {
        System.out.print("Enter Task ID to delete: ");
        int id = scanner.nextInt();
        tasks.removeIf(task -> task.getId() == id);
        saveTasks();
    }

    private static void markTaskAsCompleted(Scanner scanner) {
        System.out.print("Enter Task ID to mark as completed: ");
        int id = scanner.nextInt();
        for (Task task : tasks) {
            if (task.getId() == id) {
                task.setCompleted(true);
                saveTasks();
                return;
            }
        }
        System.out.println("Task not found.");
    }
}
