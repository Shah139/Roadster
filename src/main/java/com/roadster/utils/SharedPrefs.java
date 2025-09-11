package com.roadster.utils;

import java.util.prefs.Preferences;
import java.util.Base64;

public class SharedPrefs {
    private static final String PREF_EMAIL = "user_email";
    private static final String PREF_PASSWORD = "user_password";
    private static final String PREF_REMEMBER = "remember_me";
    
    private static final Preferences prefs = Preferences.userRoot().node("com.roadster.app");
    
    public static void saveLoginCredentials(String email, String password, boolean remember) {
        if (remember) {
            // Simple base64 encoding (not secure, but simple)
            String encodedPassword = Base64.getEncoder().encodeToString(password.getBytes());
            prefs.put(PREF_EMAIL, email);
            prefs.put(PREF_PASSWORD, encodedPassword);
            prefs.putBoolean(PREF_REMEMBER, true);
        } else {
            clearLoginCredentials();
        }
    }
    
    public static String getSavedEmail() {
        return prefs.get(PREF_EMAIL, "");
    }
    
    public static String getSavedPassword() {
        String encodedPassword = prefs.get(PREF_PASSWORD, "");
        if (!encodedPassword.isEmpty()) {
            return new String(Base64.getDecoder().decode(encodedPassword));
        }
        return "";
    }
    
    public static boolean isRememberMe() {
        return prefs.getBoolean(PREF_REMEMBER, false);
    }
    
    public static void clearLoginCredentials() {
        prefs.remove(PREF_EMAIL);
        prefs.remove(PREF_PASSWORD);
        prefs.remove(PREF_REMEMBER);
    }
}