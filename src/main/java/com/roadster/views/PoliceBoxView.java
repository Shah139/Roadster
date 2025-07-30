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

public class PoliceBoxView extends HBox {
    private MainController mainController;
    private VBox sidebar;
    private VBox mainContent;
    private TextField searchField;
    private ComboBox<String> districtFilter;
    private ComboBox<String> statusFilter;
    private VBox policeBoxList;
    private ObservableList<PoliceBox> policeBoxesData;
    private FilteredList<PoliceBox> filteredPoliceBoxes;
    
    public PoliceBoxView(MainController mainController) {
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
        searchField.setPromptText("Search police boxes...");
        
        // Filter dropdowns
        districtFilter = new ComboBox<>();
        districtFilter.getItems().addAll("All Districts", "Chattogram", "Dhaka", "Rajshahi", "Khulna", "Sylhet", "Barisal", "Rangpur", "Mymensingh");
        districtFilter.setValue("All Districts");
        
        statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("All Status", "Active", "Maintenance", "Offline");
        statusFilter.setValue("All Status");
        
        // Police boxes data
        policeBoxesData = FXCollections.observableArrayList();
        filteredPoliceBoxes = new FilteredList<>(policeBoxesData, s -> true);
        
        // Police boxes list container
        policeBoxList = new VBox(10);
        policeBoxList.setPadding(new Insets(20));
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
        
        // Police box content
        VBox policeContent = createPoliceContent();
        
        mainContent.getChildren().addAll(header, policeContent);
        return mainContent;
    }
    
    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));

        // Back to Dashboard Button
        Button backButton = new Button("← Back to Dashboard");
        backButton.getStyleClass().add("back-btn");
        backButton.setOnAction(e -> handleNavigation("Dashboard"));
        // City title
        VBox cityTitle = new VBox(5);
        
        Text cityName = new Text("Chattogram");
        cityName.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        cityName.setFill(Color.valueOf("#2c3e50"));
        
        Text systemName = new Text("Police Box Management System");
        systemName.setFont(Font.font("Segoe UI", 14));
        systemName.setFill(Color.valueOf("#7f8c8d"));
        
        cityTitle.getChildren().addAll(cityName, systemName);
        
        // User profile
        HBox userProfile = new HBox(10);
        userProfile.setAlignment(Pos.CENTER_RIGHT);
        
        Circle userAvatar = new Circle(25);
        userAvatar.setFill(Color.valueOf("#3498db"));
        
        VBox userInfo = new VBox(2);
        Text userName = new Text("Admin User");
        userName.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        userName.setFill(Color.valueOf("#2c3e50"));
        
        Text userRole = new Text("Police Supervisor");
        userRole.setFont(Font.font("Segoe UI", 12));
        userRole.setFill(Color.valueOf("#7f8c8d"));
        
        userInfo.getChildren().addAll(userName, userRole);
        userProfile.getChildren().addAll(userAvatar, userInfo);
        
        header.getChildren().addAll(backButton,cityTitle, userProfile);
        HBox.setHgrow(cityTitle, Priority.ALWAYS);
        
        return header;
    }
    
    private VBox createPoliceContent() {
        VBox policeContent = new VBox(20);
        
        // Search and filter section
        HBox searchFilterSection = createSearchFilterSection();
        
        // Statistics overview
        HBox statsOverview = createStatsOverview();
        
        // Police box list
        ScrollPane scrollPane = new ScrollPane(policeBoxList);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.getStyleClass().add("police-box-scroll-pane");
        
        policeContent.getChildren().addAll(searchFilterSection, statsOverview, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        
        return policeContent;
    }
    
    private HBox createSearchFilterSection() {
        HBox searchFilterSection = new HBox(20);
        searchFilterSection.setAlignment(Pos.CENTER_LEFT);
        
        // Search container
        HBox searchContainer = new HBox(8);
        searchContainer.setAlignment(Pos.CENTER_LEFT);
        searchContainer.getStyleClass().add("search-container");
        
        Text searchIcon = new Text("🔍");
        searchIcon.setFont(Font.font(14));
        
        searchField.setPrefWidth(300);
        searchField.setPrefHeight(35);
        
        searchContainer.getChildren().addAll(searchIcon, searchField);
        
        // Filter controls
        HBox filterControls = new HBox(10);
        filterControls.setAlignment(Pos.CENTER_RIGHT);
        
        districtFilter.setPrefWidth(150);
        statusFilter.setPrefWidth(150);
        
        filterControls.getChildren().addAll(districtFilter, statusFilter);
        
        searchFilterSection.getChildren().addAll(searchContainer, filterControls);
        HBox.setHgrow(searchContainer, Priority.ALWAYS);
        
        return searchFilterSection;
    }
    
    private HBox createStatsOverview() {
        HBox statsOverview = new HBox(20);
        statsOverview.setAlignment(Pos.CENTER_LEFT);
        
        // Active boxes stat
        VBox activeStat = createStatCard("🛡️", "142", "Active Boxes", "active");
        
        // Maintenance stat
        VBox maintenanceStat = createStatCard("🔧", "8", "Under Maintenance", "maintenance");
        
        // Offline stat
        VBox offlineStat = createStatCard("⚠️", "3", "Offline", "offline");
        
        // Total stat
        VBox totalStat = createStatCard("🧮", "153", "Total Boxes", "total");
        
        statsOverview.getChildren().addAll(activeStat, maintenanceStat, offlineStat, totalStat);
        return statsOverview;
    }
    
    private VBox createStatCard(String icon, String value, String label, String type) {
        VBox statCard = new VBox(10);
        statCard.setAlignment(Pos.CENTER);
        statCard.setPadding(new Insets(20));
        statCard.getStyleClass().add("stat-card");
        statCard.setPrefWidth(200);
        statCard.setPrefHeight(120);
        
        // Icon
        Circle iconCircle = new Circle(25);
        iconCircle.setFill(getStatColor(type));
        
        Text iconText = new Text(icon);
        iconText.setFont(Font.font(16));
        iconText.setFill(Color.WHITE);
        
        StackPane iconPane = new StackPane(iconCircle, iconText);
        
        // Value
        Text valueText = new Text(value);
        valueText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        valueText.setFill(Color.valueOf("#2c3e50"));
        
        // Label
        Text labelText = new Text(label);
        labelText.setFont(Font.font("Segoe UI", 12));
        labelText.setFill(Color.valueOf("#7f8c8d"));
        
        statCard.getChildren().addAll(iconPane, valueText, labelText);
        return statCard;
    }
    
    private Color getStatColor(String type) {
        return switch (type) {
            case "active" -> Color.valueOf("#27ae60");
            case "maintenance" -> Color.valueOf("#f39c12");
            case "offline" -> Color.valueOf("#e74c3c");
            case "total" -> Color.valueOf("#3498db");
            default -> Color.valueOf("#95a5a6");
        };
    }
    
    private void loadSampleData() {
        // Add sample police boxes
        policeBoxesData.addAll(
            new PoliceBox("PB-001", "Times Square Station", "Broadway & 42nd Street", "Active", "Manhattan"),
            new PoliceBox("PB-002", "Central Park North", "5th Ave & 110th Street", "Active", "Manhattan"),
            new PoliceBox("PB-003", "Brooklyn Bridge Plaza", "Cadman Plaza East", "Maintenance", "Brooklyn"),
            new PoliceBox("PB-004", "Queens Plaza", "Queens Blvd & 28th Street", "Offline", "Queens"),
            new PoliceBox("PB-005", "Wall Street Plaza", "Wall Street & Broadway", "Active", "Manhattan"),
            new PoliceBox("PB-006", "Yankee Stadium", "161st Street & River Ave", "Active", "Bronx"),
            new PoliceBox("PB-007", "Grand Central", "42nd Street & Park Ave", "Active", "Manhattan"),
            new PoliceBox("PB-008", "Empire State Building", "5th Ave & 34th Street", "Maintenance", "Manhattan"),
            new PoliceBox("PB-009", "Brooklyn Museum", "Eastern Parkway", "Active", "Brooklyn"),
            new PoliceBox("PB-010", "Flushing Meadows", "Corona Park", "Active", "Queens")
        );
        
        updatePoliceBoxList();
    }
    
    private void updatePoliceBoxList() {
        policeBoxList.getChildren().clear();
        
        for (PoliceBox policeBox : filteredPoliceBoxes) {
            HBox policeBoxItem = createPoliceBoxItem(policeBox);
            policeBoxList.getChildren().add(policeBoxItem);
        }
    }
    
    private HBox createPoliceBoxItem(PoliceBox policeBox) {
        HBox policeBoxItem = new HBox(15);
        policeBoxItem.setAlignment(Pos.CENTER_LEFT);
        policeBoxItem.setPadding(new Insets(15));
        policeBoxItem.getStyleClass().add("police-box-item");
        policeBoxItem.setPrefHeight(80);
        
        // Police box icon
        Circle boxIcon = new Circle(20);
        boxIcon.setFill(Color.valueOf("#3498db"));
        
        Text boxIconText = new Text("👮");
        boxIconText.setFont(Font.font(12));
        boxIconText.setFill(Color.WHITE);
        
        StackPane iconPane = new StackPane(boxIcon, boxIconText);
        
        // Police box details
        VBox boxDetails = new VBox(4);
        
        HBox basicInfo = new HBox(10);
        basicInfo.setAlignment(Pos.CENTER_LEFT);
        
        Text boxId = new Text(policeBox.getId());
        boxId.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        boxId.setFill(Color.valueOf("#2c3e50"));
        
        Text boxName = new Text(policeBox.getName());
        boxName.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        boxName.setFill(Color.valueOf("#2c3e50"));
        
        basicInfo.getChildren().addAll(boxId, boxName);
        
        Text boxLocation = new Text(policeBox.getLocation());
        boxLocation.setFont(Font.font("Segoe UI", 12));
        boxLocation.setFill(Color.valueOf("#7f8c8d"));
        
        boxDetails.getChildren().addAll(basicInfo, boxLocation);
        
        // Status badge
        HBox statusBadge = new HBox(8);
        statusBadge.setAlignment(Pos.CENTER);
        statusBadge.setPadding(new Insets(4, 12, 4, 12));
        statusBadge.getStyleClass().add("status-badge");
        statusBadge.getStyleClass().add(policeBox.getStatus().toLowerCase());
        
        Circle statusDot = new Circle(4);
        statusDot.setFill(getStatusColor(policeBox.getStatus()));
        
        Text statusText = new Text(policeBox.getStatus());
        statusText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        statusText.setFill(Color.WHITE);
        
        statusBadge.getChildren().addAll(statusDot, statusText);
        
        // Action buttons
        HBox actionButtons = new HBox(8);
        
        Button viewButton = new Button("👁️");
        viewButton.getStyleClass().add("action-btn-small");
        viewButton.setOnAction(e -> viewPoliceBox(policeBox));
        
        Button editButton = new Button("✏️");
        editButton.getStyleClass().add("action-btn-small");
        editButton.setOnAction(e -> editPoliceBox(policeBox));
        
        actionButtons.getChildren().addAll(viewButton, editButton);
        
        policeBoxItem.getChildren().addAll(iconPane, boxDetails, statusBadge, actionButtons);
        HBox.setHgrow(boxDetails, Priority.ALWAYS);
        
        return policeBoxItem;
    }
    
    private Color getStatusColor(String status) {
        return switch (status) {
            case "Active" -> Color.valueOf("#27ae60");
            case "Maintenance" -> Color.valueOf("#f39c12");
            case "Offline" -> Color.valueOf("#e74c3c");
            default -> Color.valueOf("#95a5a6");
        };
    }
    
    private void setupStyling() {
        getStyleClass().add("police-box-view");
    }
    
    private void setupEventHandlers() {
        // Search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredPoliceBoxes.setPredicate(policeBox -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                
                String lowerCaseFilter = newValue.toLowerCase();
                return policeBox.getName().toLowerCase().contains(lowerCaseFilter) ||
                       policeBox.getId().toLowerCase().contains(lowerCaseFilter) ||
                       policeBox.getLocation().toLowerCase().contains(lowerCaseFilter);
            });
            updatePoliceBoxList();
        });
        
        // District filter
        districtFilter.setOnAction(e -> {
            String selectedDistrict = districtFilter.getValue();
            filteredPoliceBoxes.setPredicate(policeBox -> {
                if ("All Districts".equals(selectedDistrict)) {
                    return true;
                }
                return policeBox.getDistrict().equals(selectedDistrict);
            });
            updatePoliceBoxList();
        });
        
        // Status filter
        statusFilter.setOnAction(e -> {
            String selectedStatus = statusFilter.getValue();
            filteredPoliceBoxes.setPredicate(policeBox -> {
                if ("All Status".equals(selectedStatus)) {
                    return true;
                }
                return policeBox.getStatus().equals(selectedStatus);
            });
            updatePoliceBoxList();
        });
    }

    
    private void viewPoliceBox(PoliceBox policeBox) {
        System.out.println("Viewing police box: " + policeBox.getName());
        // TODO: Open police box details dialog
    }
    
    private void editPoliceBox(PoliceBox policeBox) {
        System.out.println("Editing police box: " + policeBox.getName());
        // TODO: Open edit police box dialog
    }
    
    // Inner class for Police Box data
    public static class PoliceBox {
        private String id;
        private String name;
        private String location;
        private String status;
        private String district;
        
        public PoliceBox(String id, String name, String location, String status, String district) {
            this.id = id;
            this.name = name;
            this.location = location;
            this.status = status;
            this.district = district;
        }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getLocation() { return location; }
        public String getStatus() { return status; }
        public String getDistrict() { return district; }
        
        // Setters
        public void setId(String id) { this.id = id; }
        public void setName(String name) { this.name = name; }
        public void setLocation(String location) { this.location = location; }
        public void setStatus(String status) { this.status = status; }
        public void setDistrict(String district) { this.district = district; }
    }
    private void handleNavigation(String destination) {
        System.out.println("Navigating to: " + destination);
        // TODO: Implement navigation to other views
        mainController.showDashboardView();
    }
} 