package com.roadster.utils;

import com.roadster.models.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * CSV-based User Management System
 * Handles user authentication, registration, and data management using CSV file
 */
public class UserManager {
    private static final String CSV_FILE = "src/main/resources/data/users.csv";
    private static final String CSV_HEADER = "email,password,fullName,role,userId";
    
    // Current logged-in user
    private static User currentUser = null;
    private static int nextUserId = 1;
    
    static {
        initializeCSV();
        loadNextUserId();
    }
    
    /**
     * Initialize CSV file if it doesn't exist
     */
    private static void initializeCSV() {
        try {
            File file = new File(CSV_FILE);
            if (!file.exists()) {
                try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                    writer.println(CSV_HEADER);
                    // Add default admin user - format: email,password,fullName,role,userId
                    writer.println("admin@roadster.com," + encodePassword("admin123") + ",Administrator,Admin,USR-001");
                }
                System.out.println("Created users.csv with default admin user");
                System.out.println("Default login: admin@roadster.com / admin123");
            }
        } catch (IOException e) {
            System.err.println("Error initializing CSV file: " + e.getMessage());
        }
    }
    
    /**
     * Load the next available user ID
     */
    private static void loadNextUserId() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            reader.readLine(); // Skip header
            int maxId = 0;
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) { // Need all 5 columns
                    String idStr = parts[4].replace("USR-", ""); // userId is at index 4
                    try {
                        int id = Integer.parseInt(idStr);
                        maxId = Math.max(maxId, id);
                    } catch (NumberFormatException e) {
                        // Skip invalid IDs
                    }
                }
            }
            nextUserId = maxId + 1;
        } catch (IOException e) {
            nextUserId = 1;
        }
    }
    
    /**
     * Register a new user and add to CSV
     */
    public static boolean registerUser(String email, String password, String fullName, String role) {
        try {
            // Check if email already exists
            if (isEmailExists(email)) {
                return false;
            }
            
            String userId = String.format("USR-%03d", nextUserId);
            String encodedPassword = encodePassword(password);
            
            // Append to CSV file - format: email,password,fullName,role,userId
            try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_FILE, true))) {
                writer.println(email + "," + encodedPassword + "," + fullName + "," + role + "," + userId);
            }
            
            nextUserId++;
            System.out.println("User registered successfully: " + email + " (" + userId + ")");
            return true;
            
        } catch (IOException e) {
            System.err.println("Error registering user: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Validate user credentials and log them in
     */
    public static boolean loginUser(String email, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            reader.readLine(); // Skip header
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                
                if (parts.length >= 5) {
                    String storedEmail = parts[0];      // email is first column
                    String storedPassword = parts[1];   // password is second column
                    String encodedInputPassword = encodePassword(password);
                    
                    if (storedEmail.equals(email) && storedPassword.equals(encodedInputPassword)) {
                        // Login successful - create current user object
                        // CSV format: email,password,fullName,role,userId
                        currentUser = new User(parts[4], parts[0], parts[2], parts[3]); // userId, email, fullName, role
                        System.out.println("Login successful: " + parts[0] + " (" + parts[4] + ")");
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error during login: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Check if email already exists in CSV
     */
    private static boolean isEmailExists(String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            reader.readLine(); // Skip header
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].equals(email)) { // email is first column
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error checking email existence: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get current logged-in user
     */
    public static User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Get current user's full name
     */
    public static String getCurrentUserFullName() {
        return currentUser != null ? currentUser.getFullName() : "Guest User";
    }
    
    /**
     * Get current user's role
     */
    public static String getCurrentUserRole() {
        return currentUser != null ? currentUser.getRole() : "User";
    }
    
    /**
     * Get current user's email
     */
    public static String getCurrentUserEmail() {
        return currentUser != null ? currentUser.getEmail() : "";
    }
    
    /**
     * Get current user's ID
     */
    public static String getCurrentUserId() {
        return currentUser != null ? currentUser.getUsername() : "";
    }
    
    /**
     * Logout current user
     */
    public static void logout() {
        if (currentUser != null) {
            System.out.println("User logged out: " + currentUser.getEmail());
            currentUser = null;
        }
    }
    
    /**
     * Update user role in CSV
     */
    public static boolean updateUserRole(String newRole) {
        if (currentUser == null) return false;
        
        try {
            List<String> lines = Files.readAllLines(Paths.get(CSV_FILE));
            boolean updated = false;
            
            for (int i = 1; i < lines.size(); i++) { // Skip header
                String[] parts = lines.get(i).split(",");
                if (parts.length >= 5 && parts[1].equals(currentUser.getEmail())) {
                    // Update role in CSV line
                    parts[4] = newRole;
                    lines.set(i, String.join(",", parts));
                    
                    // Update current user object
                    currentUser.setRole(newRole);
                    updated = true;
                    break;
                }
            }
            
            if (updated) {
                Files.write(Paths.get(CSV_FILE), lines);
                System.out.println("User role updated to: " + newRole);
                return true;
            }
        } catch (IOException e) {
            System.err.println("Error updating user role: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Update user password in CSV
     */
    public static boolean updateUserPassword(String currentPassword, String newPassword) {
        if (currentUser == null) return false;
        
        try {
            List<String> lines = Files.readAllLines(Paths.get(CSV_FILE));
            boolean updated = false;
            
            for (int i = 1; i < lines.size(); i++) { // Skip header
                String[] parts = lines.get(i).split(",");
                if (parts.length >= 5 && parts[0].equals(currentUser.getEmail())) {
                    // Verify current password (password is in index 1)
                    if (parts[1].equals(encodePassword(currentPassword))) {
                        // Update password
                        parts[1] = encodePassword(newPassword);
                        lines.set(i, String.join(",", parts));
                        updated = true;
                        break;
                    } else {
                        return false; // Current password incorrect
                    }
                }
            }
            
            if (updated) {
                Files.write(Paths.get(CSV_FILE), lines);
                System.out.println("Password updated successfully");
                return true;
            }
        } catch (IOException e) {
            System.err.println("Error updating password: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Get all users as a list (for admin purposes)
     */
    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            reader.readLine(); // Skip header
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    User user = new User(parts[1], parts[1], parts[3], parts[4]);
                    user.setUsername(parts[0]); // Set ID as username
                    users.add(user);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
        
        return users;
    }
    
    /**
     * Simple password encoding (Base64 for demo purposes)
     */
    private static String encodePassword(String password) {
        return Base64.getEncoder().encodeToString(password.getBytes());
    }
    
    /**
     * Check if user is logged in
     */
    public static boolean isLoggedIn() {
        return currentUser != null;
    }
    
    /**
     * Print CSV contents for debugging
     */
    public static void printUsersDebug() {
        System.out.println("=== Users in CSV ===");
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV: " + e.getMessage());
        }
        System.out.println("==================");
    }
}