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
    public UserProfileView(MainController mainController) {
        this.mainController = mainController;
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

        // Main Content
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));
        mainContent.setAlignment(Pos.TOP_LEFT);
        mainContent.setStyle("-fx-background-color: transparent;");
        mainContent.setPrefWidth(0);
        mainContent.setMinWidth(0);
        mainContent.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(mainContent, Priority.ALWAYS);

        // Header
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
        Button saveBtn = new Button("Save Changes");
        saveBtn.setStyle("-fx-background-color: #4f46e5; -fx-text-fill: white; -fx-font-weight: 500; -fx-padding: 12 20; -fx-border-radius: 8; -fx-background-radius: 8;");
        header.getChildren().addAll(backBtn, headerTitle, spacer, saveBtn);

        // Profile Card
        VBox profileCard = new VBox(20);
        profileCard.setStyle("-fx-background-color: white; -fx-border-radius: 16; -fx-background-radius: 16; -fx-padding: 30; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 10, 0, 4, 0);");
        profileCard.setMaxWidth(Double.MAX_VALUE);
        profileCard.setPrefWidth(Double.MAX_VALUE);
        VBox.setVgrow(profileCard, Priority.ALWAYS);

        // Profile Header
        HBox profileHeader = new HBox(20);
        profileHeader.setAlignment(Pos.CENTER_LEFT);
        StackPane avatarPane = new StackPane();
        avatarPane.setPrefSize(80, 80);
        avatarPane.setStyle("-fx-background-radius: 40; -fx-background-color: linear-gradient(to right, #667eea, #764ba2);");
        Label avatarIcon = new Label("👤"); // User emoji
        avatarIcon.setFont(Font.font(32));
        avatarIcon.setTextFill(javafx.scene.paint.Color.WHITE);
        avatarPane.getChildren().add(avatarIcon);
        VBox profileInfo = new VBox(4);
        Text nameText = new Text(UserManager.getCurrentUserFullName() != null ? 
                                 UserManager.getCurrentUserFullName() : "Guest User");
        nameText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        nameText.setFill(javafx.scene.paint.Color.web("#1e293b"));
        Text roleText = new Text(UserManager.getCurrentUserRole() != null ? 
                               UserManager.getCurrentUserRole() : "User");
        roleText.setFont(Font.font("Segoe UI", 14));
        roleText.setFill(javafx.scene.paint.Color.web("#64748b"));
        profileInfo.getChildren().addAll(nameText, roleText);
        profileHeader.getChildren().addAll(avatarPane, profileInfo);

        // User Information Section
        VBox userInfoSection = new VBox(10);
        Text userInfoTitle = new Text("User Information");
        userInfoTitle.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 18));
        userInfoTitle.setFill(javafx.scene.paint.Color.web("#1e293b"));
        TextField userIdField = new TextField("USR-2024-001");
        userIdField.setEditable(false);
        userIdField.setStyle("-fx-background-color: #f8fafc; -fx-text-fill: #64748b;");
        TextField emailField = new TextField(UserManager.getCurrentUserEmail() != null ? 
                                             UserManager.getCurrentUserEmail() : "guest@example.com");
        emailField.setEditable(false);
        emailField.setStyle("-fx-background-color: #f8fafc; -fx-text-fill: #64748b;");
        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll("User", "Driver");
        roleCombo.setValue("Driver");
        userInfoSection.getChildren().addAll(userInfoTitle, new Label("User ID"), userIdField, new Label("Email Address"), emailField, new Label("Role"), roleCombo);

        // Password Section
        VBox passwordSection = new VBox(10);
        Text passwordTitle = new Text("Change Password");
        passwordTitle.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 18));
        passwordTitle.setFill(javafx.scene.paint.Color.web("#1e293b"));
        PasswordField currentPassword = new PasswordField();
        currentPassword.setPromptText("Enter current password");
        PasswordField newPassword = new PasswordField();
        newPassword.setPromptText("Enter new password");
        PasswordField confirmPassword = new PasswordField();
        confirmPassword.setPromptText("Confirm new password");
        Button changePasswordBtn = new Button("Update Password");
        changePasswordBtn.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-padding: 12 20; -fx-border-radius: 8; -fx-background-radius: 8;");
        passwordSection.getChildren().addAll(passwordTitle, new Label("Current Password"), currentPassword, new Label("New Password"), newPassword, new Label("Confirm New Password"), confirmPassword, changePasswordBtn);

        // Settings Section
        VBox settingsSection = new VBox(10);
        Text settingsTitle = new Text("Settings");
        settingsTitle.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 18));
        settingsTitle.setFill(javafx.scene.paint.Color.web("#1e293b"));
        CheckBox emailNotif = new CheckBox("Email Notifications");
        emailNotif.setSelected(true);
        CheckBox smsAlerts = new CheckBox("SMS Alerts");
        CheckBox locationTracking = new CheckBox("Location Tracking");
        locationTracking.setSelected(true);
        settingsSection.getChildren().addAll(settingsTitle, emailNotif, smsAlerts, locationTracking);

        profileCard.getChildren().addAll(profileHeader, userInfoSection, passwordSection, settingsSection);
        mainContent.getChildren().addAll(header, profileCard);

        getChildren().addAll(sidebar, mainContent);
    }
}
