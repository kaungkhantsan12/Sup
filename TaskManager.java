import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.Serializable;
import java.util.Date;
import java.util.*;

class Task implements Serializable {
    private String name;
    private Date dueDate;
    private boolean completed;

    public Task(String name, Date dueDate) {
        this.name = name;
        this.dueDate = dueDate;
        this.completed = false;
    }

    public String getName() {
        return name;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void markAsCompleted() {
        completed = true;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return "Task: " + name + ", Due Date: " + dateFormat.format(dueDate) + ", Completed: " + completed;
    }
}

public class TaskManager {
    private static List<Task> tasks = new ArrayList<>();
    private static final String FILE_NAME = "tasks.txt";

    public static void main(String[] args) {
        loadTasksFromFile();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("===== Task Manager =====");
            System.out.println("1. Add Task");
            System.out.println("2. Mark Task as Completed");
            System.out.println("3. View Tasks");
            System.out.println("4. Delete Completed Tasks");
            System.out.println("5. Sort Tasks by Due Date");
            System.out.println("6. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    addTask(scanner);
                    break;
                case 2:
                    markTaskAsCompleted(scanner);
                    break;
                case 3:
                    viewTasks();
                    break;
                case 4:
                    deleteCompletedTasks();
                    break;
                case 5:
                    sortTasksByDueDate();
                    break;
                case 6:
                    saveTasksToFile();
                    System.out.println("Exiting Task Manager. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addTask(Scanner scanner) {
        System.out.println("Enter task name:");
        String name = scanner.nextLine();

        System.out.println("Enter due date (yyyy-MM-dd):");
        String dueDateString = scanner.nextLine();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dueDate = dateFormat.parse(dueDateString);
            Task task = new Task(name, dueDate);
            tasks.add(task);
            System.out.println("Task added successfully!");
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
        }
    }

    private static void markTaskAsCompleted(Scanner scanner) {
        System.out.println("Enter the name of the task to mark as completed:");
        String taskName = scanner.nextLine();

        for (Task task : tasks) {
            if (task.getName().equalsIgnoreCase(taskName)) {
                task.markAsCompleted();
                System.out.println("Task marked as completed!");
                return;
            }
        }

        System.out.println("Task not found.");
    }

    private static void viewTasks() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("===== Viewing Tasks =====");
        System.out.println("1. View Pending Taks");
        System.out.println("2. View Completed Taks");
        System.out.println("3. View All Tasks");

        int choiceOfTask = scanner.nextInt();
                    scanner.nextLine(); 
        
                    switch (choiceOfTask) {
                        case 1:
                            viewPendingTasks();
                            break;
                        case 2:
                            viewCompletedTasks();
                            break;
                        case 3:
                            viewPendingTasks();
                            viewCompletedTasks();
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }

        
    }

    private static void viewPendingTasks() {
        System.out.println("===== Pending Tasks =====");
        for (Task task : tasks) {
            if (!task.isCompleted()) {
                System.out.println(task);
            }
        }
    }

    private static void viewCompletedTasks() {
        System.out.println("===== Completed Tasks =====");
        for (Task task : tasks) {
            if (task.isCompleted()) {
                System.out.println(task);
            }
        }
    }

    private static void deleteCompletedTasks() {
        tasks.removeIf(Task::isCompleted);
        System.out.println("Completed tasks deleted successfully!");
    }

    private static void sortTasksByDueDate() {
        Collections.sort(tasks, Comparator.comparing(Task::getDueDate));
        System.out.println("Tasks sorted by due date.");
    }

    @SuppressWarnings("unchecked")
    private static void loadTasksFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            tasks = (List<Task>) ois.readObject();
            System.out.println("Tasks loaded from file.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No previous tasks found. Starting with an empty task list.");
        }
    }

    private static void saveTasksToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(tasks);
            System.out.println("Tasks saved to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
