package com.roadster.views;

import com.roadster.components.SideBar;
import com.roadster.controllers.MainController;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import com.roadster.models.Driver;

/**
 * Drivers View - JavaFX equivalent of drivers
 * This will contain driver management functionality
 */
public class DriversView extends HBox {
    private MainController mainController;
    private VBox sidebar;
    private VBox mainContent;
    private TextField searchField;
    private Button addDriverButton;
    private VBox driversList;
    private ObservableList<Driver> driversData;
    private FilteredList<Driver> filteredDrivers;
    
    public DriversView(MainController mainController) {
        this.mainController = mainController;
        initializeComponents();
        setupLayout();
        setupStyling();
        setupEventHandlers();
        loadSampleData();
    }
    
    private void initializeComponents() {
        // Search field
        searchField = new TextField();
        searchField.setPromptText("Search drivers...");
        
        // Add driver button
        addDriverButton = new Button("➕ Add Driver");
        addDriverButton.getStyleClass().add("action-btn");
        
        // Drivers data
        driversData = FXCollections.observableArrayList();
        filteredDrivers = new FilteredList<>(driversData, s -> true);
        
        // Drivers list container
        driversList = new VBox(10);
        driversList.setPadding(new Insets(20));
    }
    
    private void setupLayout() {
        setSpacing(0);
        
        // Sidebar
        SideBar sideBarComponent = new SideBar(mainController);
        sidebar = sideBarComponent.createSidebar();
        
        // Main content
        mainContent = createMainContent();
        
        getChildren().addAll(sidebar, mainContent);
        HBox.setHgrow(sidebar, Priority.NEVER);
        HBox.setHgrow(mainContent, Priority.ALWAYS);
    }
    


    
    private VBox createMainContent() {
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));
        mainContent.getStyleClass().add("main-content");
        
        // Header
        HBox header = createHeader();
        
        // Page content
        VBox pageContent = createPageContent();
        
        mainContent.getChildren().addAll(header, pageContent);
        return mainContent;
    }
    
    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));
        
        // Header left
        VBox headerLeft = new VBox(8);
        
        Button backButton = new Button("← Back to Dashboard");
        backButton.getStyleClass().add("back-btn");
        backButton.setOnAction(e -> handleNavigation("Dashboard"));
        
        Text cityName = new Text("Chattogram");
        cityName.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        cityName.setFill(Color.valueOf("#2c3e50"));
        
        headerLeft.getChildren().addAll(backButton, cityName);
        
        // Header right
        HBox headerRight = new HBox(15);
        headerRight.setAlignment(Pos.CENTER_RIGHT);
        
        // Search input
        HBox searchContainer = new HBox(8);
        searchContainer.setAlignment(Pos.CENTER_LEFT);
        searchContainer.getStyleClass().add("search-container");
        
        Text searchIcon = new Text("🔍");
        searchIcon.setFont(Font.font(14));
        
        searchField.setPrefWidth(300);
        searchField.setPrefHeight(35);
        
        searchContainer.getChildren().addAll(searchIcon, searchField);
        
        headerRight.getChildren().addAll(searchContainer, addDriverButton);
        
        header.getChildren().addAll(headerLeft, headerRight);
        HBox.setHgrow(headerLeft, Priority.ALWAYS);
        
        return header;
    }
    
    private VBox createPageContent() {
        VBox pageContent = new VBox(20);
        
        // Page header
        HBox pageHeader = createPageHeader();
        
        // Drivers list
        ScrollPane scrollPane = new ScrollPane(driversList);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.getStyleClass().add("drivers-scroll-pane");
        
        pageContent.getChildren().addAll(pageHeader, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        
        return pageContent;
    }
    
    private HBox createPageHeader() {
        HBox pageHeader = new HBox(20);
        pageHeader.setAlignment(Pos.CENTER_LEFT);
        
        Text title = new Text("Registered Drivers");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        title.setFill(Color.valueOf("#2c3e50"));
        
        // Stats summary
        HBox statsSummary = new HBox(30);
        statsSummary.setAlignment(Pos.CENTER_RIGHT);
        
        VBox totalDrivers = createStatItem("156", "Total Drivers");
        VBox activeDrivers = createStatItem("142", "Active");
        VBox suspendedDrivers = createStatItem("14", "Suspended");
        
        statsSummary.getChildren().addAll(totalDrivers, activeDrivers, suspendedDrivers);
        
        pageHeader.getChildren().addAll(title, statsSummary);
        HBox.setHgrow(title, Priority.ALWAYS);
        
        return pageHeader;
    }
    
    private VBox createStatItem(String number, String label) {
        VBox statItem = new VBox(4);
        statItem.setAlignment(Pos.CENTER);
        
        Text numberText = new Text(number);
        numberText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        numberText.setFill(Color.valueOf("#3498db"));
        
        Text labelText = new Text(label);
        labelText.setFont(Font.font("Segoe UI", 12));
        labelText.setFill(Color.valueOf("#7f8c8d"));
        
        statItem.getChildren().addAll(numberText, labelText);
        return statItem;
    }
    
    private void loadSampleData() {
        driversData.clear();
        
        // Add realistic driver data matching the original web design
        driversData.add(new Driver("001", "John Cena", "DL-2024-001", "+1-555-0101", "john.cena@email.com"));
        driversData.add(new Driver("002", "Mohammed Ali", "DL-2024-002", "+1-555-0102", "mohammed.ali@email.com"));
        driversData.add(new Driver("003", "Fatima Ahmed", "DL-2024-003", "+1-555-0103", "fatima.ahmed@email.com"));
        driversData.add(new Driver("004", "Omar Khalil", "DL-2024-004", "+1-555-0104", "omar.khalil@email.com"));
        driversData.add(new Driver("005", "Sara Ibrahim", "DL-2024-005", "+1-555-0105", "sara.ibrahim@email.com"));
        driversData.add(new Driver("006", "Youssef Mohamed", "DL-2024-006", "+1-555-0106", "youssef.mohamed@email.com"));
        driversData.add(new Driver("007", "Nadia Hassan", "DL-2024-007", "+1-555-0107", "nadia.hassan@email.com"));
        driversData.add(new Driver("008", "Ahmed Hassan", "DL-2024-008", "+1-555-0108", "ahmed.hassan@email.com"));
        driversData.add(new Driver("009", "Layla Mansour", "DL-2024-009", "+1-555-0109", "layla.mansour@email.com"));
        driversData.add(new Driver("010", "Karim El-Sayed", "DL-2024-010", "+1-555-0110", "karim.elsayed@email.com"));
        driversData.add(new Driver("011", "Aisha Rahman", "DL-2024-011", "+1-555-0111", "aisha.rahman@email.com"));
        driversData.add(new Driver("012", "Hassan Al-Zahra", "DL-2024-012", "+1-555-0112", "hassan.alzahra@email.com"));
        driversData.add(new Driver("013", "Mariam Khalil", "DL-2024-013", "+1-555-0113", "mariam.khalil@email.com"));
        driversData.add(new Driver("014", "Tariq Ibrahim", "DL-2024-014", "+1-555-0114", "tariq.ibrahim@email.com"));
        driversData.add(new Driver("015", "Zainab Ahmed", "DL-2024-015", "+1-555-0115", "zainab.ahmed@email.com"));
        
        // Set some drivers as suspended
        driversData.get(3).setStatus("Suspended"); // Omar Khalil
        driversData.get(9).setStatus("Suspended"); // Karim El-Sayed
        driversData.get(13).setStatus("Suspended"); // Tariq Ibrahim
        
        // Assign locations to match the original design
        driversData.get(0).setVehicleAssigned("Downtown Area");
        driversData.get(1).setVehicleAssigned("City Center");
        driversData.get(2).setVehicleAssigned("North District");
        driversData.get(3).setVehicleAssigned("South Zone");
        driversData.get(4).setVehicleAssigned("East Area");
        driversData.get(5).setVehicleAssigned("West District");
        driversData.get(6).setVehicleAssigned("Central Plaza");
        driversData.get(7).setVehicleAssigned("Business District");
        driversData.get(8).setVehicleAssigned("Residential Area");
        driversData.get(9).setVehicleAssigned("Industrial Zone");
        driversData.get(10).setVehicleAssigned("University District");
        driversData.get(11).setVehicleAssigned("Shopping Mall");
        driversData.get(12).setVehicleAssigned("Hospital Area");
        driversData.get(13).setVehicleAssigned("Airport Zone");
        driversData.get(14).setVehicleAssigned("Sports Complex");
        
        updateDriversList();
    }
    
    private void updateDriversList() {
        driversList.getChildren().clear();
        
        for (Driver driver : filteredDrivers) {
            HBox driverItem = createDriverItem(driver);
            driversList.getChildren().add(driverItem);
        }
    }
    
    private HBox createDriverItem(Driver driver) {
        HBox driverItem = new HBox(15);
        driverItem.setAlignment(Pos.CENTER_LEFT);
        driverItem.setPadding(new Insets(15));
        driverItem.getStyleClass().add("driver-item");
        driverItem.setPrefHeight(80);
        
        // Driver avatar
        Circle avatar = new Circle(25);
        avatar.setFill(Color.valueOf("#3498db"));
        
        Text avatarText = new Text(driver.getName().substring(0, 1));
        avatarText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        avatarText.setFill(Color.WHITE);
        
        StackPane avatarPane = new StackPane(avatar, avatarText);
        
        // Driver details
        VBox driverDetails = new VBox(4);
        
        Text driverName = new Text(driver.getName());
        driverName.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        driverName.setFill(Color.valueOf("#2c3e50"));
        
        Text driverLicense = new Text("License: " + driver.getLicenseNumber());
        driverLicense.setFont(Font.font("Segoe UI", 12));
        driverLicense.setFill(Color.valueOf("#7f8c8d"));
        
        // Use the location from vehicleAssigned field (repurposed for location)
        Text driverLocation = new Text(driver.getVehicleAssigned());
        driverLocation.setFont(Font.font("Segoe UI", 12));
        driverLocation.setFill(Color.valueOf("#7f8c8d"));
        
        driverDetails.getChildren().addAll(driverName, driverLicense, driverLocation);
        
        // Driver status
        VBox statusSection = new VBox(10);
        statusSection.setAlignment(Pos.CENTER_RIGHT);
        
        // Status badge
        HBox statusBadge = new HBox(8);
        statusBadge.setAlignment(Pos.CENTER);
        statusBadge.setPadding(new Insets(4, 12, 4, 12));
        statusBadge.getStyleClass().add("status-badge");
        
        if ("Suspended".equals(driver.getStatus())) {
            statusBadge.getStyleClass().add("suspended");
        } else {
            statusBadge.getStyleClass().add("active");
        }
        
        Circle statusDot = new Circle(4);
        statusDot.setFill("Suspended".equals(driver.getStatus()) ? Color.valueOf("#e74c3c") : Color.valueOf("#27ae60"));
        
        Text statusText = new Text(driver.getStatus());
        statusText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        statusText.setFill(Color.WHITE);
        
        statusBadge.getChildren().addAll(statusDot, statusText);
        
        // Action buttons
        HBox actionButtons = new HBox(8);
        
        Button editButton = new Button("✏️");
        editButton.getStyleClass().add("action-btn-small");
        editButton.setOnAction(e -> editDriver(driver));
        
        Button deleteButton = new Button("🗑️");
        deleteButton.getStyleClass().add("action-btn-small");
        deleteButton.setOnAction(e -> deleteDriver(driver));
        
        actionButtons.getChildren().addAll(editButton, deleteButton);
        
        // Layout
        HBox.setHgrow(driverDetails, Priority.ALWAYS);
        statusSection.getChildren().addAll(statusBadge, actionButtons);
        driverItem.getChildren().addAll(avatarPane, driverDetails, statusSection);
        
        return driverItem;
    }
    
    private void setupStyling() {
        getStyleClass().add("drivers-view");
    }
    
    private void setupEventHandlers() {
        // Search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredDrivers.setPredicate(driver -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                
                String lowerCaseFilter = newValue.toLowerCase();
                return driver.getName().toLowerCase().contains(lowerCaseFilter) ||
                       driver.getLicenseNumber().toLowerCase().contains(lowerCaseFilter) ||
                       driver.getEmail().toLowerCase().contains(lowerCaseFilter);
            });
            updateDriversList();
        });
        
        // Add driver button
        addDriverButton.setOnAction(e -> addNewDriver());
    }

    private void handleNavigation(String destination) {
        System.out.println("Navigating to: " + destination);
        // TODO: Implement navigation to other views
        mainController.showDashboardView();
    }
    
    private void addNewDriver() {
        // TODO: Open add driver dialog
        System.out.println("Adding new driver...");
        
        // For now, add a sample driver
        Driver newDriver = new Driver(
            String.valueOf(driversData.size() + 1),
            "New Driver " + (driversData.size() + 1),
            "DL-2024-" + String.format("%03d", driversData.size() + 1),
            "+1-555-" + String.format("%04d", (int)(Math.random() * 10000)),
            "driver" + (driversData.size() + 1) + "@email.com"
        );
        
        driversData.add(newDriver);
        updateDriversList();
    }
    
    private void editDriver(Driver driver) {
        // TODO: Open edit driver dialog
        System.out.println("Editing driver: " + driver.getName());
    }
    
    private void deleteDriver(Driver driver) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Driver");
        alert.setHeaderText("Confirm Deletion");
        alert.setContentText("Are you sure you want to delete " + driver.getName() + "?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                driversData.remove(driver);
                updateDriversList();
            }
        });
    }
} 