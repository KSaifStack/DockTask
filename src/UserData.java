/**
 * Saves the Username and Password from the file
 * Allows  the user to Manage/login to get,change,modfiy and delete tasks
 * Kinda replaced the whole idea of the Arraylist...
 * GOALS:
 * REMOVEALLTASKS: will delete all tasks saved in file
 * REMOVEALLDATA: will remove all users saved in file
 * Plugins: Add a feature that would allow the user to store the name or a id for the plugin based off the user
 * Support new lines for task desc- should be able to make new lines when saving the desc.
 @param username the user's name
 @param password the user's password
  * @author  Kareem
 * @version November 17, 2025
 */

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class UserData {

    //Data File(Does create by itself if removed :D)
    private static final String LogData = "User.txt";
    private static final String TaskData = "Task.txt";

    //Storing Dates/Time
    //SaveData("JohnDoe", "SecurePassword123");
    public static void saveUser(String newuser, String newpassword) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LogData, true))) {
            writer.write(newuser + ":" + newpassword);
            writer.newLine();
            System.out.println("User credentials saved successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while saving user credentials: " + e.getMessage());
        }
    }

    //Finds the User within the file using BufferedReader and Arrays
    public static boolean findUser(String user, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(LogData))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(":");
                if (credentials.length == 2 && credentials[0].equals(user) && credentials[1].equals(password)) {
                    return true;
                }
            }
            //Throws Exception otherwise.
        } catch (IOException e) {
            System.out.println("Error While checking" + e.getMessage());
        }
        return false;
    }

    //Add Task to data
    public static void SaveTask(String username, String taskname, String taskdescription, int taskrank, String taskgroup, LocalDateTime dueDate) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TaskData, true))) {
            DateTimeFormatter dataformatted = DateTimeFormatter.ofPattern("yyyy MM dd hh mm a");
            String formattedDate = dueDate.format(dataformatted);
            //writer.write(username + ":" + taskname + ":" + taskdescription + ":" + taskrank + ":" + taskgroup + ":" + formattedDate);
            //Subject to change
            String escapedDescription = taskdescription.replace("\n", "\\n");
            writer.write(username + ":" + taskname + ":" + escapedDescription + ":" + taskrank + ":" + taskgroup + ":" + formattedDate);

            writer.newLine();
            writer.close();
            System.out.println("Task data saved successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while saving task data: " + e.getMessage());
        }
    }

    //Remove Task inside data
    //allow it to read the lines within the file
    //make it find the task lines your looking to delete
    public static void removeTask(String username, String taskname) {
        File inputFile = new File(TaskData);
        File tempFile = new File("temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length >= 4 && parts[0].equals(username) && parts[1].equals(taskname)) {
                    continue; // skip the task to be removed
                }
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Task removed successfully");
        } catch (IOException e) {
            System.out.println("Error removing task: " + e.getMessage());
        }

        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            System.out.println("Error finalizing task removal.");
        }
    }

    //Update Task thats in data
    //Will be done by Getting User,taskname,desc and rank
    //Read lines,if line meets goal then update
    public static void updateTask(String username, String taskname, String newTaskname, String newDescription, int newRank, String TaskGroup, LocalDateTime dueDate) {
        File inputFile = new File(TaskData);
        File tempFile = new File("temp.txt");
        boolean updated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");

                if (parts.length >= 4 && parts[0].equals(username) && parts[1].equals(taskname)) {
                    // Update task info
                    taskname = newTaskname;
                    DateTimeFormatter dataformatted = DateTimeFormatter.ofPattern("yyyy MM dd hh mm a");
                    String formattedDate = dueDate.format(dataformatted);
                    String escapedDescription = newDescription.replace("\n", "\\n");
                    writer.write(username + ":" + taskname + ":" + escapedDescription + ":" + newRank + ":" + TaskGroup + ":" + formattedDate);
                    updated = true;
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error updating task: " + e.getMessage());
            return;
        }

        //Ensures files and previous data are there
        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            System.out.println("Error finalizing task update.");
        } else if (updated) {
            System.out.println("Task updated successfully.");
        } else {
            System.out.println("Task not found.");
        }
    }

    //Find Tasks
    public static void FindData(String Username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(TaskData))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length >= 6 && parts[0].trim().toLowerCase().equals(Username)) {
                    //String username, String taskname, String taskdescription, int taskrank, String taskgroup,DueDate
                    String description = parts[2].replace("\\n", "\n");
                    System.out.println("Task: " + parts[1] + ", Description: " + description + ", Rank: " + parts[3] + ", Group: " + parts[4] + ", Due: " + parts[5]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error While checking" + e.getMessage());
        }
    }
    //Returns data in a 2d Array(groupOfTasks(Tasks)==String[][])
    public static String[][] ReturnData(String Username) {
        List<String[]> taskList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(TaskData))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length >= 6 && parts[0].trim().equalsIgnoreCase(Username)) {
                    // Add task info to the list (excluding username)
                    String[] task = new String[]{
                            parts[1].trim(), // taskname
                            parts[2].replace("\\n", "\n").trim(), // taskdescription (unescaped)
                            parts[3].trim(), // taskrank
                            parts[4].trim(), // taskgroup
                            parts[5].trim()  // dueDate

                    };
                    taskList.add(task);
                }
            }
        } catch (IOException e) {
            System.out.println("Error while checking: " + e.getMessage());
        }

        return taskList.toArray(new String[0][0]);
    }

    //Username checker
    public static boolean usernameExists(String Username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(LogData))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(":");
                if (credentials.length == 2 && credentials[0].equals(Username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error While checking" + e.getMessage());
        }
        return false;

        //Checks Data using BufferedReader and LocalDataTime 
    }
    //Checks date of task if its passed its given date.
    public static void DateChecker(String TaskName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(TaskData))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length >= 6 && parts[1].equalsIgnoreCase(TaskName)) {
                    DateTimeFormatter dataformatted = DateTimeFormatter.ofPattern("yyyy MM dd hh mm a");
                    LocalDateTime dueDate = LocalDateTime.parse(parts[5], dataformatted);
                    String formattedDate = dueDate.format(dataformatted);
                    LocalDateTime now = LocalDateTime.now();

                    System.out.println(" This assignment is due at: " + formattedDate );

                    if (dueDate.isBefore(now)) {
                        System.out.println("This task is past due!");
                    } else if (dueDate.minusHours(24).isBefore(now)) {
                        System.out.println("Due in less than 24 hours!");
                    }
                    return;
                }
            }
            System.out.println("Task not found.");
        } catch (Exception e) {
            System.out.println("Error checking task: " + e.getMessage());
        }

    }

    //Find task time
    public static LocalDateTime DataCheckerUI(String TaskName) {
        LocalDateTime dueDate = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(TaskData))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length >= 6 && parts[1].equalsIgnoreCase(TaskName)) {
                    DateTimeFormatter dataformatted = DateTimeFormatter.ofPattern("yyyy MM dd hh mm a");
                    dueDate = LocalDateTime.parse(parts[5], dataformatted);
                    String formattedDate = dueDate.format(dataformatted);
                    LocalDateTime now = LocalDateTime.now();
                    System.out.println(TaskName+" is due at: " + formattedDate);

                }
            }
            System.out.println("Task not found.");
        } catch (Exception e) {
            System.out.println("Error checking task: " + e.getMessage());
        }
        return dueDate;
    }
}


    


    
