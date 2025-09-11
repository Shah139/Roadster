package com.roadster.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;
import java.util.Base64;

public class UserStorage {
    private static final String USERS_PREFIX = "registered_user_";
    private static final Preferences prefs = Preferences.userRoot().node("com.roadster.users");
    
    public static void registerUser(String email, String password, String fullName, String role) {
        // Hash the password before storing
        String hashedPassword = hashPassword(password);
        String userData = String.join("|", hashedPassword, fullName, role);
        prefs.put(USERS_PREFIX + email, userData);
    }
    
    public static boolean isUserRegistered(String email) {
        return prefs.get(USERS_PREFIX + email, null) != null;
    }
    
    public static boolean validateCredentials(String email, String password) {
        String userData = prefs.get(USERS_PREFIX + email, null);
        if (userData != null) {
            String[] parts = userData.split("\\|");
            String storedHashedPassword = parts[0];
            return storedHashedPassword.equals(hashPassword(password));
        }
        return false;
    }
    
    private static String hashPassword(String password) {
        // Simple hashing for demo purposes
        return Base64.getEncoder().encodeToString(password.getBytes());
    }
}
