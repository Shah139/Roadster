package com.roadster.components;

import com.roadster.controllers.MainController;
import com.roadster.utils.UserManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class SideBar extends VBox{
    private MainController mainController;
    public SideBar(MainController mainController) {
        this.mainController = mainController;
    }
    public VBox createSidebar() {
        VBox sidebar = new VBox(30);
        sidebar.setPrefWidth(250);
        sidebar.setPadding(new Insets(20));
        sidebar.getStyleClass().add("sidebar");

        // Logo
        HBox logo = new HBox(12);
        logo.setAlignment(Pos.CENTER_LEFT);

        Circle logoIcon = new Circle(20);
        logoIcon.setFill(Color.valueOf("#3498db"));

        Text logoText = new Text("Roadster");
        logoText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        logoText.setFill(Color.valueOf("#2c3e50"));

        logo.getChildren().addAll(logoIcon, logoText);

        // Navigation menu
        VBox navMenu = new VBox(10);

        // Dashboard item (active)
        HBox dashboardItem = createNavItem("📊", "Dashboard", true);
        // Other nav items (Police Box removed)
        HBox mapsItem = createNavItem("🗺️", "Interactive Map", false);
        HBox profileItem = createNavItem("👤", "User Profile", false);

        navMenu.getChildren().addAll(dashboardItem, mapsItem, profileItem);

        // Spacer to push logout to bottom
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // User info section
        VBox userSection = createUserSection();

        // Logout section
        VBox logoutSection = createLogoutSection();

        sidebar.getChildren().addAll(logo, navMenu, spacer, userSection, logoutSection);
        return sidebar;
    }
    public HBox createNavItem(String icon, String text, boolean isActive) {
        HBox item = new HBox(12);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(12, 16, 12, 16));
        item.setPrefHeight(50);
        item.getStyleClass().add("nav-item");

        if (isActive) {
            item.getStyleClass().add("active");
        }

        Text iconText = new Text(icon);
        iconText.setFont(Font.font(16));

        Text label = new Text(text);
        label.setFont(Font.font("Segoe UI", 14));

        item.getChildren().addAll(iconText, label);

        // Add click handler
        item.setOnMouseClicked(e -> handleNavigation(text));

        return item;
    }
    private void handleNavigation(String destination) {
        System.out.println("Navigating to: " + destination);
        // TODO: Implement navigation to other views
        if(destination == "Dashboard"){
            mainController.showDashboardView();
        }else if(destination == "User Profile"){
            mainController.showUserProfileView();
        }else if(destination == "Interactive Map"){
            mainController.showMapsView();
        }
    }

    private VBox createUserSection() {
        VBox userSection = new VBox(8);
        userSection.setPadding(new Insets(16, 16, 8, 16));
        userSection.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 12; -fx-border-radius: 12;");

        // User avatar and info
        HBox userInfo = new HBox(12);
        userInfo.setAlignment(Pos.CENTER_LEFT);

        Circle userAvatar = new Circle(16);
        userAvatar.setFill(Color.valueOf("#667eea"));

        VBox userDetails = new VBox(2);
        Text userName = new Text(UserManager.getCurrentUserFullName() != null ? 
                                UserManager.getCurrentUserFullName() : "Guest User");
        userName.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        userName.setFill(Color.valueOf("#2c3e50"));

        Text userRole = new Text(UserManager.getCurrentUserRole() != null ? 
                               UserManager.getCurrentUserRole() : "User");
        userRole.setFont(Font.font("Segoe UI", 10));
        userRole.setFill(Color.valueOf("#64748b"));

        userDetails.getChildren().addAll(userName, userRole);
        userInfo.getChildren().addAll(userAvatar, userDetails);

        userSection.getChildren().add(userInfo);
        return userSection;
    }

    private VBox createLogoutSection() {
        VBox logoutSection = new VBox(8);
        logoutSection.setPadding(new Insets(8, 0, 0, 0));

        // Logout button
        HBox logoutItem = new HBox(12);
        logoutItem.setAlignment(Pos.CENTER_LEFT);
        logoutItem.setPadding(new Insets(12, 16, 12, 16));
        logoutItem.setPrefHeight(50);
        logoutItem.setStyle("-fx-background-color: #fee2e2; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand;");

        Text logoutIcon = new Text("🚪");
        logoutIcon.setFont(Font.font(16));

        Text logoutLabel = new Text("Logout");
        logoutLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        logoutLabel.setFill(Color.valueOf("#dc2626"));

        logoutItem.getChildren().addAll(logoutIcon, logoutLabel);

        // Add hover effects
        logoutItem.setOnMouseEntered(e -> {
            logoutItem.setStyle("-fx-background-color: #fecaca; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand;");
        });

        logoutItem.setOnMouseExited(e -> {
            logoutItem.setStyle("-fx-background-color: #fee2e2; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand;");
        });

        // Add logout functionality
        logoutItem.setOnMouseClicked(e -> handleLogout());

        logoutSection.getChildren().add(logoutItem);
        return logoutSection;
    }

    private void handleLogout() {
        // Show confirmation dialog
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Logout Confirmation");
        confirmDialog.setHeaderText("Are you sure you want to logout?");
        confirmDialog.setContentText("You will be redirected to the login page.");

        // Customize button text
        ButtonType yesButton = new ButtonType("Yes, Logout");
        ButtonType cancelButton = new ButtonType("Cancel");
        confirmDialog.getButtonTypes().setAll(yesButton, cancelButton);

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == yesButton) {
                performLogout();
            }
        });
    }

    private void performLogout() {
        try {
            // Clear user session data using UserManager logout method
            UserManager.logout();

            System.out.println("User logged out successfully");

            // Navigate back to login view
            if (mainController != null) {
                mainController.showLoginView();
            }

        } catch (Exception e) {
            System.err.println("Error during logout: " + e.getMessage());
            
            // Show error dialog
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Logout Error");
            errorAlert.setHeaderText("An error occurred during logout");
            errorAlert.setContentText("Please try again or restart the application.");
            errorAlert.showAndWait();
        }
    }
}