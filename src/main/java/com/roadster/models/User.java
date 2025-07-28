package com.roadster.models;

import javafx.beans.property.*;

/**
 * User model class for user management
 */
public class User {
    private final StringProperty username;
    private final StringProperty email;
    private final StringProperty fullName;
    private final StringProperty role;
    private final BooleanProperty isActive;
    
    public User(String username, String email, String fullName, String role) {
        this.username = new SimpleStringProperty(username);
        this.email = new SimpleStringProperty(email);
        this.fullName = new SimpleStringProperty(fullName);
        this.role = new SimpleStringProperty(role);
        this.isActive = new SimpleBooleanProperty(true);
    }
    
    // Username property
    public String getUsername() { return username.get(); }
    public void setUsername(String username) { this.username.set(username); }
    public StringProperty usernameProperty() { return username; }
    
    // Email property
    public String getEmail() { return email.get(); }
    public void setEmail(String email) { this.email.set(email); }
    public StringProperty emailProperty() { return email; }
    
    // Full name property
    public String getFullName() { return fullName.get(); }
    public void setFullName(String fullName) { this.fullName.set(fullName); }
    public StringProperty fullNameProperty() { return fullName; }
    
    // Role property
    public String getRole() { return role.get(); }
    public void setRole(String role) { this.role.set(role); }
    public StringProperty roleProperty() { return role; }
    
    // Active property
    public boolean isActive() { return isActive.get(); }
    public void setActive(boolean active) { this.isActive.set(active); }
    public BooleanProperty activeProperty() { return isActive; }
    
    @Override
    public String toString() {
        return "User{" +
                "username='" + getUsername() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", role='" + getRole() + '\'' +
                ", isActive=" + isActive() +
                '}';
    }
} 