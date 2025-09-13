package com.roadster.views;

import com.roadster.components.SideBar;
import com.roadster.controllers.MainController;
import com.roadster.utils.UserManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * User Profile View - JavaFX equivalent of user-profile
 * This will contain user profile management functionality
 */
public class UserProfileView extends HBox {
    private MainController mainController;
    
    // Form control fields
    private TextField userIdField;
    private TextField emailField;
    private TextField nameField;
    private ComboBox<String> roleCombo;
    private PasswordField currentPasswordField;
    private PasswordField newPasswordField;
    private PasswordField confirmPasswordField;
    private CheckBox emailNotificationBox;
    private CheckBox smsAlertsBox;
    private CheckBox locationTrackingBox;
    private Button saveChangesButton;
    private Button changePasswordButton;
    public UserProfileView(MainController mainController) {
        this.mainController = mainController;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    private void initializeComponents() {
        // Initialize form controls
        userIdField = new TextField("USR-2024-001");
        userIdField.setEditable(false);
        userIdField.setStyle("-fx-background-color: #f8fafc; -fx-text-fill: #64748b;");
        userIdField.setPrefWidth(300);
        userIdField.setMaxWidth(400);
        
        emailField = new TextField(UserManager.getCurrentUserEmail() != null ? 
                                   UserManager.getCurrentUserEmail() : "guest@example.com");
        emailField.setEditable(false);
        emailField.setStyle("-fx-background-color: #f8fafc; -fx-text-fill: #64748b;");
        emailField.setPrefWidth(300);
        emailField.setMaxWidth(400);
        
        nameField = new TextField(UserManager.getCurrentUserFullName() != null ? 
                                  UserManager.getCurrentUserFullName() : "Guest User");
        nameField.setStyle("-fx-padding: 12; -fx-border-radius: 8; -fx-background-radius: 8;");
        nameField.setPrefWidth(300);
        nameField.setMaxWidth(400);
        
        roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll("User", "Driver");
        roleCombo.setValue(UserManager.getCurrentUserRole() != null ? 
                          UserManager.getCurrentUserRole() : "User");
        roleCombo.setStyle("-fx-padding: 12; -fx-border-radius: 8; -fx-background-radius: 8;");
        roleCombo.setPrefWidth(300);
        roleCombo.setMaxWidth(400);
        
        // Password fields
        currentPasswordField = new PasswordField();
        currentPasswordField.setPromptText("Enter current password");
        currentPasswordField.setStyle("-fx-padding: 12; -fx-border-radius: 8; -fx-background-radius: 8;");
        currentPasswordField.setPrefWidth(300);
        currentPasswordField.setMaxWidth(400);
        
        newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Enter new password (min 8 characters)");
        newPasswordField.setStyle("-fx-padding: 12; -fx-border-radius: 8; -fx-background-radius: 8;");
        newPasswordField.setPrefWidth(300);
        newPasswordField.setMaxWidth(400);
        
        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm new password");
        confirmPasswordField.setStyle("-fx-padding: 12; -fx-border-radius: 8; -fx-background-radius: 8;");
        confirmPasswordField.setPrefWidth(300);
        confirmPasswordField.setMaxWidth(400);
        
        // Settings checkboxes
        emailNotificationBox = new CheckBox("Email Notifications");
        emailNotificationBox.setSelected(true);
        
        smsAlertsBox = new CheckBox("SMS Alerts");
        smsAlertsBox.setSelected(false);
        
        locationTrackingBox = new CheckBox("Location Tracking");
        locationTrackingBox.setSelected(true);
        
        // Buttons
        saveChangesButton = new Button("Save Changes");
        saveChangesButton.setStyle("-fx-background-color: #4f46e5; -fx-text-fill: white; -fx-font-weight: 500; -fx-padding: 12 20; -fx-border-radius: 8; -fx-background-radius: 8;");
        
        changePasswordButton = new Button("Update Password");
        changePasswordButton.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-padding: 12 20; -fx-border-radius: 8; -fx-background-radius: 8;");
    }
    
    private void setupLayout() {
        setSpacing(0);
        setPadding(new Insets(0));
        setStyle("-fx-background-color: #f5f7fa;");

        // Sidebar
        SideBar sideBar = new SideBar(mainController);
        VBox sidebar = sideBar.createSidebar();
        sidebar.setStyle("-fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 10, 0, 2, 0); -fx-border-radius: 20; -fx-background-radius: 20; -fx-padding: 20; -fx-min-width: 280;");
        sidebar.setPrefWidth(280);
        sidebar.setMaxWidth(280);
        sidebar.setMinWidth(280);
        sidebar.setPadding(new Insets(20, 0, 20, 20));

        // Main Content Container
        VBox contentContainer = new VBox(20);
        contentContainer.setPadding(new Insets(20));
        contentContainer.setAlignment(Pos.TOP_LEFT);
        contentContainer.setStyle("-fx-background-color: transparent;");

        // Header
        HBox header = createHeader();
        
        // Profile Card
        VBox profileCard = createProfileCard();

        contentContainer.getChildren().addAll(header, profileCard);
        
        // Wrap content in ScrollPane
        ScrollPane scrollPane = new ScrollPane(contentContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setPrefWidth(0);
        scrollPane.setMinWidth(0);
        scrollPane.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);

        getChildren().addAll(sidebar, scrollPane);
    }
    
    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-border-radius: 16; -fx-background-radius: 16; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 10, 0, 4, 0);");
        header.setSpacing(20);

        Button backBtn = new Button("Back to Dashboard");
        backBtn.setStyle("-fx-background-color: #f1f5f9; -fx-text-fill: #64748b; -fx-font-size: 14; -fx-padding: 12 16; -fx-border-radius: 8; -fx-background-radius: 8;");
        backBtn.setOnAction(e -> mainController.showDashboardView());
        
        Text headerTitle = new Text("User Profile");
        headerTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        headerTitle.setFill(javafx.scene.paint.Color.web("#1e293b"));
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        header.getChildren().addAll(backBtn, headerTitle, spacer, saveChangesButton);
        return header;
    }
    
    private VBox createProfileCard() {
        VBox profileCard = new VBox(25);
        profileCard.setStyle("-fx-background-color: white; -fx-border-radius: 16; -fx-background-radius: 16; -fx-padding: 30; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 10, 0, 4, 0);");
        profileCard.setMaxWidth(800); // Limit the maximum width
        profileCard.setPrefWidth(600); // Preferred width
        profileCard.setAlignment(Pos.TOP_LEFT);

        // Profile Header
        HBox profileHeader = createProfileHeader();
        
        // User Information Section
        VBox userInfoSection = createUserInfoSection();
        
        // Password Section
        VBox passwordSection = createPasswordSection();
        
        // Settings Section
        VBox settingsSection = createSettingsSection();

        profileCard.getChildren().addAll(profileHeader, userInfoSection, passwordSection, settingsSection);
        return profileCard;
    }
    
    private HBox createProfileHeader() {
        HBox profileHeader = new HBox(20);
        profileHeader.setAlignment(Pos.CENTER_LEFT);
        
        StackPane avatarPane = new StackPane();
        avatarPane.setPrefSize(80, 80);
        avatarPane.setStyle("-fx-background-radius: 40; -fx-background-color: linear-gradient(to right, #667eea, #764ba2);");
        Label avatarIcon = new Label("👤");
        avatarIcon.setFont(Font.font(32));
        avatarIcon.setTextFill(javafx.scene.paint.Color.WHITE);
        avatarPane.getChildren().add(avatarIcon);
        
        VBox profileInfo = new VBox(4);
        Text nameText = new Text(nameField.getText());
        nameText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        nameText.setFill(javafx.scene.paint.Color.web("#1e293b"));
        Text roleText = new Text(roleCombo.getValue());
        roleText.setFont(Font.font("Segoe UI", 14));
        roleText.setFill(javafx.scene.paint.Color.web("#64748b"));
        profileInfo.getChildren().addAll(nameText, roleText);
        
        profileHeader.getChildren().addAll(avatarPane, profileInfo);
        return profileHeader;
    }
    
    private VBox createUserInfoSection() {
        VBox userInfoSection = new VBox(15);
        
        Text userInfoTitle = new Text("Personal Information");
        userInfoTitle.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 18));
        userInfoTitle.setFill(javafx.scene.paint.Color.web("#1e293b"));
        
        VBox nameSection = createFormField("Full Name", nameField, true);
        VBox userIdSection = createFormField("User ID", userIdField, false);
        VBox emailSection = createFormField("Email Address", emailField, false);
        VBox roleSection = createFormField("Role", roleCombo, true);
        
        userInfoSection.getChildren().addAll(userInfoTitle, nameSection, userIdSection, emailSection, roleSection);
        return userInfoSection;
    }
    
    private VBox createPasswordSection() {
        VBox passwordSection = new VBox(15);
        
        Text passwordTitle = new Text("Change Password");
        passwordTitle.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 18));
        passwordTitle.setFill(javafx.scene.paint.Color.web("#1e293b"));
        
        VBox currentPassSection = createFormField("Current Password", currentPasswordField, true);
        VBox newPassSection = createFormField("New Password", newPasswordField, true);
        VBox confirmPassSection = createFormField("Confirm New Password", confirmPasswordField, true);
        
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        buttonBox.getChildren().add(changePasswordButton);
        
        passwordSection.getChildren().addAll(passwordTitle, currentPassSection, newPassSection, confirmPassSection, buttonBox);
        return passwordSection;
    }
    
    private VBox createSettingsSection() {
        VBox settingsSection = new VBox(15);
        
        Text settingsTitle = new Text("Notification Settings");
        settingsTitle.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 18));
        settingsTitle.setFill(javafx.scene.paint.Color.web("#1e293b"));
        
        VBox checkboxContainer = new VBox(10);
        checkboxContainer.getChildren().addAll(emailNotificationBox, smsAlertsBox, locationTrackingBox);
        
        settingsSection.getChildren().addAll(settingsTitle, checkboxContainer);
        return settingsSection;
    }
    
    private VBox createFormField(String labelText, Control control, boolean editable) {
        VBox fieldSection = new VBox(8);
        fieldSection.setAlignment(Pos.CENTER_LEFT);
        fieldSection.setPrefWidth(400);
        fieldSection.setMaxWidth(400);
        
        Label label = new Label(labelText);
        label.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        label.setTextFill(javafx.scene.paint.Color.web("#374151"));
        
        // Ensure all controls have consistent sizing
        if (control instanceof TextField || control instanceof PasswordField) {
            control.setPrefWidth(300);
            control.setMaxWidth(300);
            if (!editable && control instanceof TextField) {
                control.setStyle("-fx-background-color: #f8fafc; -fx-text-fill: #64748b; -fx-padding: 12; -fx-border-radius: 8; -fx-background-radius: 8; -fx-pref-width: 300; -fx-max-width: 300;");
            }
        } else if (control instanceof ComboBox) {
            control.setPrefWidth(300);
            control.setMaxWidth(300);
        }
        
        fieldSection.getChildren().addAll(label, control);
        return fieldSection;
    }
    
    private void setupEventHandlers() {
        // Save Changes button
        saveChangesButton.setOnAction(e -> handleSaveChanges());
        
        // Change Password button
        changePasswordButton.setOnAction(e -> handlePasswordChange());
        
        // Add hover effects
        setupButtonHoverEffects();
    }
    
    private void handleSaveChanges() {
        try {
            // Validate form data
            if (!validateProfileData()) {
                return;
            }
            
            // Update user information in UserManager
            String newName = nameField.getText().trim();
            String newRole = roleCombo.getValue();
            
            // For now, we'll simulate saving to UserManager
            // In a real app, you would save to database
            System.out.println("Saving profile changes:");
            System.out.println("Name: " + newName);
            System.out.println("Role: " + newRole);
            System.out.println("Email Notifications: " + emailNotificationBox.isSelected());
            System.out.println("SMS Alerts: " + smsAlertsBox.isSelected());
            System.out.println("Location Tracking: " + locationTrackingBox.isSelected());
            
            // Show success message
            showSuccessAlert("Profile Updated", 
                           "Your profile information has been saved successfully!");
            
            // Update UserManager with new values (if methods exist)
            // UserManager.setCurrentUserFullName(newName);
            // UserManager.setCurrentUserRole(newRole);
            
        } catch (Exception e) {
            showErrorAlert("Save Error", "Failed to save profile changes: " + e.getMessage());
        }
    }
    
    private void handlePasswordChange() {
        try {
            // Validate password change
            if (!validatePasswordChange()) {
                return;
            }
            
            String currentPassword = currentPasswordField.getText();
            String newPassword = newPasswordField.getText();
            
            // Actually update the password in CSV file using UserManager
            boolean passwordUpdated = UserManager.updateUserPassword(currentPassword, newPassword);
            
            if (passwordUpdated) {
                // Clear password fields on success
                currentPasswordField.clear();
                newPasswordField.clear();
                confirmPasswordField.clear();
                
                // Show success message
                showSuccessAlert("Password Updated", 
                               "Your password has been changed successfully!\nYou can now use your new password for future logins.");
                
                System.out.println("Password successfully updated in CSV file for user: " + UserManager.getCurrentUserEmail());
            } else {
                // Password update failed - likely incorrect current password
                showErrorAlert("Password Error", 
                             "Failed to update password. Please verify your current password is correct.");
            }
            
        } catch (Exception e) {
            showErrorAlert("Password Error", "Failed to change password: " + e.getMessage());
        }
    }
    
    private boolean validateProfileData() {
        StringBuilder errors = new StringBuilder();
        
        if (nameField.getText().trim().isEmpty()) {
            errors.append("• Name cannot be empty\n");
        }
        
        if (nameField.getText().trim().length() < 2) {
            errors.append("• Name must be at least 2 characters long\n");
        }
        
        if (roleCombo.getValue() == null) {
            errors.append("• Please select a role\n");
        }
        
        if (errors.length() > 0) {
            showErrorAlert("Validation Error", "Please fix the following issues:\n\n" + errors.toString());
            return false;
        }
        
        return true;
    }
    
    private boolean validatePasswordChange() {
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
        StringBuilder errors = new StringBuilder();
        
        if (currentPassword.isEmpty()) {
            errors.append("• Current password is required\n");
        }
        
        if (newPassword.isEmpty()) {
            errors.append("• New password is required\n");
        }
        
        if (confirmPassword.isEmpty()) {
            errors.append("• Password confirmation is required\n");
        }
        
        if (!newPassword.isEmpty() && newPassword.length() < 8) {
            errors.append("• New password must be at least 8 characters long\n");
        }
        
        if (!newPassword.isEmpty() && !confirmPassword.isEmpty() && !newPassword.equals(confirmPassword)) {
            errors.append("• New password and confirmation do not match\n");
        }
        
        // Check password strength
        if (!newPassword.isEmpty() && !isPasswordStrong(newPassword)) {
            errors.append("• Password should contain at least one uppercase letter, one lowercase letter, and one number\n");
        }
        
        if (errors.length() > 0) {
            showErrorAlert("Password Validation Error", "Please fix the following issues:\n\n" + errors.toString());
            return false;
        }
        
        return true;
    }
    
    private boolean isPasswordStrong(String password) {
        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        
        return hasUpper && hasLower && hasDigit;
    }
    
    private void setupButtonHoverEffects() {
        // Save button hover effects
        saveChangesButton.setOnMouseEntered(e -> 
            saveChangesButton.setStyle("-fx-background-color: #4338ca; -fx-text-fill: white; -fx-font-weight: 500; -fx-padding: 12 20; -fx-border-radius: 8; -fx-background-radius: 8;"));
        
        saveChangesButton.setOnMouseExited(e -> 
            saveChangesButton.setStyle("-fx-background-color: #4f46e5; -fx-text-fill: white; -fx-font-weight: 500; -fx-padding: 12 20; -fx-border-radius: 8; -fx-background-radius: 8;"));
        
        // Change password button hover effects
        changePasswordButton.setOnMouseEntered(e -> 
            changePasswordButton.setStyle("-fx-background-color: #059669; -fx-text-fill: white; -fx-padding: 12 20; -fx-border-radius: 8; -fx-background-radius: 8;"));
        
        changePasswordButton.setOnMouseExited(e -> 
            changePasswordButton.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-padding: 12 20; -fx-border-radius: 8; -fx-background-radius: 8;"));
    }
    
    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText("Success!");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
