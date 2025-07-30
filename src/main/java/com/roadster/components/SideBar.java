package com.roadster.components;

import com.roadster.controllers.MainController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
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
        // Other nav items
        HBox mapsItem = createNavItem("🗺️", "Interactive Map", false);
        HBox profileItem = createNavItem("👤", "User Profile", false);

        navMenu.getChildren().addAll(dashboardItem, mapsItem, profileItem);

        sidebar.getChildren().addAll(logo, navMenu);
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
}
