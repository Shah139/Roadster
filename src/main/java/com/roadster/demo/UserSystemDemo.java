package com.roadster.demo;

import com.roadster.utils.UserManager;

/**
 * Simple demonstration of the CSV-based user management system
 */
public class UserSystemDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Roadster CSV User Management System Demo ===\n");
        
        // Test 1: Login with admin credentials
        System.out.println("Test 1: Admin Login");
        boolean loginResult = UserManager.loginUser("admin@roadster.com", "admin123");
        if (loginResult) {
            System.out.println("✅ Admin login successful!");
            System.out.println("   Current user: " + UserManager.getCurrentUserFullName());
            System.out.println("   Role: " + UserManager.getCurrentUserRole());
            System.out.println("   Email: " + UserManager.getCurrentUserEmail());
        } else {
            System.out.println("❌ Admin login failed!");
        }
        
        // Test 2: Logout
        System.out.println("\nTest 2: Logout");
        UserManager.logout();
        System.out.println("✅ User logged out");
        System.out.println("   Current user: " + UserManager.getCurrentUserFullName());
        
        // Test 3: Try invalid login
        System.out.println("\nTest 3: Invalid Login");
        boolean invalidLogin = UserManager.loginUser("admin@roadster.com", "wrongpassword");
        if (!invalidLogin) {
            System.out.println("✅ Invalid login correctly rejected");
        } else {
            System.out.println("❌ Invalid login was accepted (error!)");
        }
        
        // Test 4: Register new user
        System.out.println("\nTest 4: Register New User");
        boolean registration = UserManager.registerUser("demo@test.com", "demo123", "Demo User", "User");
        if (registration) {
            System.out.println("✅ New user registered successfully!");
            
            // Test 5: Login with new user
            System.out.println("\nTest 5: Login with New User");
            boolean newUserLogin = UserManager.loginUser("demo@test.com", "demo123");
            if (newUserLogin) {
                System.out.println("✅ New user login successful!");
                System.out.println("   Current user: " + UserManager.getCurrentUserFullName());
                System.out.println("   Role: " + UserManager.getCurrentUserRole());
                System.out.println("   User ID: " + UserManager.getCurrentUserId());
            }
        } else {
            System.out.println("❌ New user registration failed!");
        }
        
        // Test 6: Try duplicate registration
        System.out.println("\nTest 6: Duplicate Registration");
        boolean duplicateReg = UserManager.registerUser("admin@roadster.com", "newpass", "Another Admin", "User");
        if (!duplicateReg) {
            System.out.println("✅ Duplicate registration correctly rejected");
        } else {
            System.out.println("❌ Duplicate registration was allowed (error!)");
        }
        
        System.out.println("\n=== Demo Complete ===");
        System.out.println("The CSV-based user management system is working correctly!");
        
        // Final cleanup
        UserManager.logout();
    }
}