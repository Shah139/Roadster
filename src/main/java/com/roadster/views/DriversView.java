package com.roadster.views;

import com.roadster.components.SideBar;
import com.roadster.controllers.MainController;
import com.roadster.models.Driver;
import com.roadster.service.ApiService;
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

import java.util.List;

public class DriversView extends HBox {
    private MainController mainController;
    private VBox sidebar;
    private VBox mainContent;
    private ComboBox<String> statusFilter;
    private VBox driversList;
    private ObservableList<Driver> driversData;
    private FilteredList<Driver> filteredDrivers;
    private Text activeStatValue;
    private Text suspendedStatValue;
    private Text offlineStatValue;
    private Text totalStatValue;
    private Text selectedDistrictText;

    // District mapping based on DashboardView dropdown
    private static final String[] DISTRICTS = {
            "All Districts", "Chattogram", "Dhaka", "Rajshahi", "Khulna", "Sylhet", "Barisal", "Rangpur", "Mymensingh"
    };

    public DriversView(MainController mainController) {
        this.mainController = mainController;
        initializeComponents();
        setupLayout();
        setupStyling();
        setupEventHandlers();
        loadDriversFromApi();
    }

    private void loadDriversFromApi() {
        new Thread(() -> {
            try {
                // Get the selected district from DashboardView
                int selectedIndex = DashboardView.selectedCityIndex;
                final String selectedDistrict = (selectedIndex >= 0 && selectedIndex < DISTRICTS.length)
                        ? DISTRICTS[selectedIndex] : "All Districts";

                System.out.println("Loading drivers for district: " + selectedDistrict + " (index: " + selectedIndex + ")");

                final List<Driver> driversWithDistricts = new java.util.ArrayList<>();

                if ("All Districts".equals(selectedDistrict)) {
                    // Load all drivers
                    driversWithDistricts.addAll(ApiService.fetchAllDrivers());
                    System.out.println("Loading all districts");
                } else {
                    // Load drivers for specific district only
                    driversWithDistricts.addAll(ApiService.fetchDriversByDistrict(selectedDistrict));
                    System.out.println("Filtered for district: " + selectedDistrict);
                }

                System.out.println("Found " + driversWithDistricts.size() + " drivers for " + selectedDistrict);

                // Clear existing data on JavaFX thread
                javafx.application.Platform.runLater(() -> {
                    driversData.clear();

                    // Add to data and set random status
                    for (Driver driver : driversWithDistricts) {
                        driver.setStatus(generateRandomStatus());
                        driversData.add(driver);
                    }

                    // Update the UI
                    updateDriversList();
                    updateStatistics();
                    updateSelectedDistrictDisplay(selectedDistrict);
                });

            } catch (Exception e) {
                e.printStackTrace();
                javafx.application.Platform.runLater(() -> {
                    driversList.getChildren().clear();
                    driversList.getChildren().add(new Label("⚠️ Failed to load data from API: " + e.getMessage()));
                });
            }
        }).start();
    }

    private String generateRandomStatus() {
        // Generate random status for demo purposes
        String[] statuses = {"Active", "Active", "Active", "Suspended", "Offline"}; // More Active drivers
        return statuses[(int)(Math.random() * statuses.length)];
    }

    private void updateSelectedDistrictDisplay(String selectedDistrict) {
        if (selectedDistrictText != null) {
            selectedDistrictText.setText("Showing: " + selectedDistrict);
        }
    }

    private void updateStatistics() {
        int activeCount = 0;
        int suspendedCount = 0;
        int offlineCount = 0;
        int totalCount = driversData.size();

        for (Driver driver : driversData) {
            switch (driver.getStatus()) {
                case "Active" -> activeCount++;
                case "Suspended" -> suspendedCount++;
                case "Offline" -> offlineCount++;
            }
        }

        // Update the stat values
        if (activeStatValue != null) {
            activeStatValue.setText(String.valueOf(activeCount));
        }
        if (suspendedStatValue != null) {
            suspendedStatValue.setText(String.valueOf(suspendedCount));
        }
        if (offlineStatValue != null) {
            offlineStatValue.setText(String.valueOf(offlineCount));
        }
        if (totalStatValue != null) {
            totalStatValue.setText(String.valueOf(totalCount));
        }
    }

    private void initializeComponents() {
        statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("All Status", "Active", "Suspended", "Offline");
        statusFilter.setValue("All Status");

        driversData = FXCollections.observableArrayList();
        filteredDrivers = new FilteredList<>(driversData, s -> true);

        driversList = new VBox(10);
        driversList.setPadding(new Insets(20));
    }

    private void setupLayout() {
        setSpacing(0);

        SideBar sideBarComponent = new SideBar(mainController);
        sidebar = sideBarComponent.createSidebar();
        mainContent = createMainContent();

        getChildren().addAll(sidebar, mainContent);
        HBox.setHgrow(sidebar, Priority.NEVER);
        HBox.setHgrow(mainContent, Priority.ALWAYS);
    }

    private VBox createMainContent() {
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));
        mainContent.getStyleClass().add("main-content");

        HBox header = createHeader();
        VBox driversContent = createDriversContent();

        mainContent.getChildren().addAll(header, driversContent);
        return mainContent;
    }

    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));

        Button backButton = new Button("← Back to Dashboard");
        backButton.getStyleClass().add("back-btn");
        backButton.setOnAction(e -> handleNavigation("Dashboard"));

        VBox Title = new VBox(5);

        Text systemName = new Text("Drivers Management System");
        systemName.setFont(Font.font("Segoe UI", 14));
        systemName.setFill(Color.valueOf("#7f8c8d"));

        // Add selected district display
        selectedDistrictText = new Text("Showing: All Districts");
        selectedDistrictText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        selectedDistrictText.setFill(Color.valueOf("#3498db"));

        Title.getChildren().addAll(systemName, selectedDistrictText);

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

        // Add refresh button to reload with current district selection
        Button refreshButton = new Button("🔄 Refresh");
        refreshButton.getStyleClass().add("refresh-btn");
        refreshButton.setOnAction(e -> loadDriversFromApi());

        header.getChildren().addAll(backButton, Title, refreshButton, userProfile);
        HBox.setHgrow(Title, Priority.ALWAYS);

        return header;
    }

    private VBox createDriversContent() {
        VBox driversContent = new VBox(20);

        // Filter section with status filter and district info
        HBox filterSection = new HBox(20);
        filterSection.setAlignment(Pos.CENTER_LEFT);

        statusFilter.setPrefWidth(150);

        // Add district change button
        Button changeDistrictButton = new Button("Change District");
        changeDistrictButton.getStyleClass().add("secondary-btn");
        changeDistrictButton.setOnAction(e -> {
            mainController.showDashboardView(); // Go back to dashboard to change district
        });

        filterSection.getChildren().addAll(statusFilter, changeDistrictButton);

        HBox statsOverview = createStatsOverview();

        ScrollPane scrollPane = new ScrollPane(driversList);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.getStyleClass().add("police-box-scroll-pane");

        driversContent.getChildren().addAll(filterSection, statsOverview, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        return driversContent;
    }

    private HBox createStatsOverview() {
        HBox statsOverview = new HBox(20);
        statsOverview.setAlignment(Pos.CENTER_LEFT);

        VBox activeStat = createStatCard("🛡️", "Active Drivers", "active");
        VBox suspendedStat = createStatCard("🔧", "Suspended", "suspended");
        VBox offlineStat = createStatCard("⚠️", "Offline", "offline");
        VBox totalStat = createStatCard("🧮", "Total Drivers", "total");

        statsOverview.getChildren().addAll(activeStat, suspendedStat, offlineStat, totalStat);
        return statsOverview;
    }

    private VBox createStatCard(String icon, String label, String type) {
        VBox statCard = new VBox(10);
        statCard.setAlignment(Pos.CENTER);
        statCard.setPadding(new Insets(20));
        statCard.getStyleClass().add("stat-card");
        statCard.setPrefWidth(200);
        statCard.setPrefHeight(120);

        Circle iconCircle = new Circle(25);
        iconCircle.setFill(getStatColor(type));

        Text iconText = new Text(icon);
        iconText.setFont(Font.font(16));
        iconText.setFill(Color.WHITE);

        StackPane iconPane = new StackPane(iconCircle, iconText);

        // dynamic value text
        Text valueText = new Text("0");
        valueText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        valueText.setFill(Color.valueOf("#2c3e50"));

        Text labelText = new Text(label);
        labelText.setFont(Font.font("Segoe UI", 12));
        labelText.setFill(Color.valueOf("#7f8c8d"));

        statCard.getChildren().addAll(iconPane, valueText, labelText);

        // keep references
        switch (type) {
            case "active" -> activeStatValue = valueText;
            case "suspended" -> suspendedStatValue = valueText;
            case "offline" -> offlineStatValue = valueText;
            case "total" -> totalStatValue = valueText;
        }

        return statCard;
    }

    private Color getStatColor(String type) {
        return switch (type) {
            case "active" -> Color.valueOf("#27ae60");
            case "suspended" -> Color.valueOf("#f39c12");
            case "offline" -> Color.valueOf("#e74c3c");
            case "total" -> Color.valueOf("#3498db");
            default -> Color.valueOf("#95a5a6");
        };
    }

    private void updateDriversList() {
        driversList.getChildren().clear();

        if (filteredDrivers.isEmpty()) {
            Label noDataLabel = new Label("No drivers found for the selected criteria.");
            noDataLabel.setFont(Font.font("Segoe UI", 14));
            noDataLabel.setTextFill(Color.valueOf("#7f8c8d"));
            driversList.getChildren().add(noDataLabel);
            return;
        }

        for (Driver driver : filteredDrivers) {
            HBox driverItem = createDriverItem(driver);
            driversList.getChildren().add(driverItem);
        }
    }

    private HBox createDriverItem(Driver driver) {
        HBox driverItem = new HBox(15);
        driverItem.setAlignment(Pos.CENTER_LEFT);
        driverItem.setPadding(new Insets(15));
        driverItem.getStyleClass().add("police-box-item");
        driverItem.setPrefHeight(80);

        Circle boxIcon = new Circle(20);
        boxIcon.setFill(Color.valueOf("#3498db"));

        Text boxIconText = new Text("🚗");
        boxIconText.setFont(Font.font(12));
        boxIconText.setFill(Color.WHITE);

        StackPane iconPane = new StackPane(boxIcon, boxIconText);

        VBox boxDetails = new VBox(4);
        HBox basicInfo = new HBox(10);
        basicInfo.setAlignment(Pos.CENTER_LEFT);

        Text boxId = new Text("ID: " + driver.getDriverId());
        boxId.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        boxId.setFill(Color.valueOf("#7f8c8d"));

        Text boxName = new Text(driver.getName());
        boxName.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        boxName.setFill(Color.valueOf("#2c3e50"));

        basicInfo.getChildren().addAll(boxId);

        Text boxLocation = new Text(driver.getDistrict());
        boxLocation.setFont(Font.font("Segoe UI", 12));
        boxLocation.setFill(Color.valueOf("#7f8c8d"));

        boxDetails.getChildren().addAll(boxName, basicInfo, boxLocation);

        HBox statusBadge = new HBox(8);
        statusBadge.setAlignment(Pos.CENTER);
        statusBadge.setPadding(new Insets(4, 12, 4, 12));
        statusBadge.getStyleClass().add("status-badge");
        statusBadge.getStyleClass().add(driver.getStatus().toLowerCase());

        Circle statusDot = new Circle(4);
        statusDot.setFill(getStatusColor(driver.getStatus()));

        Text statusText = new Text(driver.getStatus());
        statusText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        statusText.setFill(Color.WHITE);

        statusBadge.getChildren().addAll(statusDot, statusText);

        HBox actionButtons = new HBox(8);
        Button viewButton = new Button("👁️");
        viewButton.getStyleClass().add("action-btn-small");
        viewButton.setOnAction(e -> viewDriver(driver));

        Button editButton = new Button("✏️");
        editButton.getStyleClass().add("action-btn-small");
        editButton.setOnAction(e -> editDriver(driver));

        actionButtons.getChildren().addAll(viewButton, editButton);

        driverItem.getChildren().addAll(iconPane, boxDetails, statusBadge, actionButtons);
        HBox.setHgrow(boxDetails, Priority.ALWAYS);

        return driverItem;
    }

    private Color getStatusColor(String status) {
        return switch (status) {
            case "Active" -> Color.valueOf("#27ae60");
            case "Suspended" -> Color.valueOf("#f39c12");
            case "Offline" -> Color.valueOf("#e74c3c");
            default -> Color.valueOf("#95a5a6");
        };
    }

    private void setupStyling() {
        getStyleClass().add("police-box-view");
    }

    private void setupEventHandlers() {
        statusFilter.setOnAction(e -> {
            String selectedStatus = statusFilter.getValue();
            filteredDrivers.setPredicate(driver -> {
                if ("All Status".equals(selectedStatus)) {
                    return true;
                }
                return driver.getStatus().equals(selectedStatus);
            });
            updateDriversList();
        });
    }

    private void viewDriver(Driver driver) {
        System.out.println("Viewing driver: " + driver.getName());
        // TODO: Implement detailed view
    }

    private void editDriver(Driver driver) {
        System.out.println("Editing driver: " + driver.getName());
        // TODO: Implement edit functionality
    }

    private void handleNavigation(String destination) {
        System.out.println("Navigating to: " + destination);
        mainController.showDashboardView();
    }
}