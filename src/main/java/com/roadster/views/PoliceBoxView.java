package com.roadster.views;

import com.roadster.components.SideBar;
import com.roadster.controllers.MainController;
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
import java.util.ArrayList;

public class PoliceBoxView extends HBox {
    private MainController mainController;
    private VBox sidebar;
    private VBox mainContent;
    private ComboBox<String> statusFilter;
    private VBox policeBoxList;
    private ObservableList<PoliceBox> policeBoxesData;
    private FilteredList<PoliceBox> filteredPoliceBoxes;
    private Text activeStatValue;
    private Text maintenanceStatValue;
    private Text offlineStatValue;
    private Text totalStatValue;
    private Text selectedDistrictText;

    // District mapping based on DashboardView dropdown
    private static final String[] DISTRICTS = {
            "All Districts", "Chattogram", "Dhaka", "Rajshahi", "Khulna", "Sylhet", "Barisal", "Rangpur", "Mymensingh"
    };

    public PoliceBoxView(MainController mainController) {
        this.mainController = mainController;
        initializeComponents();
        setupLayout();
        setupStyling();
        setupEventHandlers();
        loadPoliceStationsFromApi();
    }

    private void loadPoliceStationsFromApi() {
        new Thread(() -> {
            try {
                // Get the selected district from DashboardView
                int selectedIndex = DashboardView.selectedCityIndex;
                final String selectedDistrict = (selectedIndex >= 0 && selectedIndex < DISTRICTS.length)
                        ? DISTRICTS[selectedIndex] : "All Districts";

                System.out.println("Loading police stations for district: " + selectedDistrict + " (index: " + selectedIndex + ")");

                final List<String[]> stationsWithDistricts = new ArrayList<>();

                if ("All Districts".equals(selectedDistrict)) {
                    // Load all police stations
                    stationsWithDistricts.addAll(ApiService.fetchPoliceStationsWithDistricts());
                    System.out.println("Loading all districts");
                } else {
                    // Load stations for specific district only
                    List<String[]> allStations = ApiService.fetchPoliceStationsWithDistricts();

                    // Filter by the selected district (exact match)
                    for (String[] station : allStations) {
                        if (station.length >= 2 && station[1].trim().equalsIgnoreCase(selectedDistrict.trim())) {
                            stationsWithDistricts.add(station);
                            System.out.println("Added station: " + station[0] + " from district: " + station[1]);
                        }
                    }
                    System.out.println("Filtered for district: " + selectedDistrict);
                }

                System.out.println("Found " + stationsWithDistricts.size() + " stations for " + selectedDistrict);

                // Clear existing data on JavaFX thread
                javafx.application.Platform.runLater(() -> {
                    policeBoxesData.clear();

                    // Convert to PoliceBox objects
                    for (String[] station : stationsWithDistricts) {
                        if (station.length >= 2) {
                            PoliceBox policeBox = new PoliceBox(
                                    generateStationId(station[0]),  // Generate ID from station name
                                    station[0],                     // Station name
                                    station[1] + " District",       // Location (district + "District")
                                    generateRandomStatus(),         // Random status for demo
                                    station[1]                      // District
                            );
                            policeBoxesData.add(policeBox);
                        }
                    }

                    // Update the UI
                    updatePoliceBoxList();
                    updateStatistics();
                    updateSelectedDistrictDisplay(selectedDistrict);
                });

            } catch (Exception e) {
                e.printStackTrace();
                javafx.application.Platform.runLater(() -> {
                    policeBoxList.getChildren().clear();
                    policeBoxList.getChildren().add(new Label("⚠️ Failed to load data from API: " + e.getMessage()));
                });
            }
        }).start();
    }


    private String generateStationId(String stationName) {
        // Generate a simple ID from station name (first 3 chars + hash)
        String prefix = stationName.length() >= 3 ? stationName.substring(0, 3).toUpperCase() : stationName.toUpperCase();
        int hash = Math.abs(stationName.hashCode()) % 1000;
        return prefix + String.format("%03d", hash);
    }

    private String generateRandomStatus() {
        // Generate random status for demo purposes
        String[] statuses = {"Active", "Active", "Active", "Maintenance", "Offline"}; // More Active stations
        return statuses[(int)(Math.random() * statuses.length)];
    }

    private void updateSelectedDistrictDisplay(String selectedDistrict) {
        if (selectedDistrictText != null) {
            selectedDistrictText.setText("Showing: " + selectedDistrict);
        }
    }

    private void updateStatistics() {
        int activeCount = 0;
        int maintenanceCount = 0;
        int offlineCount = 0;
        int totalCount = policeBoxesData.size();

        for (PoliceBox policeBox : policeBoxesData) {
            switch (policeBox.getStatus()) {
                case "Active" -> activeCount++;
                case "Maintenance" -> maintenanceCount++;
                case "Offline" -> offlineCount++;
            }
        }

        // Update the stat values
        if (activeStatValue != null) {
            activeStatValue.setText(String.valueOf(activeCount));
        }
        if (maintenanceStatValue != null) {
            maintenanceStatValue.setText(String.valueOf(maintenanceCount));
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
        statusFilter.getItems().addAll("All Status", "Active", "Maintenance", "Offline");
        statusFilter.setValue("All Status");

        policeBoxesData = FXCollections.observableArrayList();
        filteredPoliceBoxes = new FilteredList<>(policeBoxesData, s -> true);

        policeBoxList = new VBox(10);
        policeBoxList.setPadding(new Insets(20));
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
        VBox policeContent = createPoliceContent();

        mainContent.getChildren().addAll(header, policeContent);
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

        Text systemName = new Text("Police Box Management System");
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
        refreshButton.setOnAction(e -> loadPoliceStationsFromApi());

        header.getChildren().addAll(backButton, Title, refreshButton, userProfile);
        HBox.setHgrow(Title, Priority.ALWAYS);

        return header;
    }

    private VBox createPoliceContent() {
        VBox policeContent = new VBox(20);

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

        ScrollPane scrollPane = new ScrollPane(policeBoxList);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.getStyleClass().add("police-box-scroll-pane");

        policeContent.getChildren().addAll(filterSection, statsOverview, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        return policeContent;
    }

    private HBox createStatsOverview() {
        HBox statsOverview = new HBox(20);
        statsOverview.setAlignment(Pos.CENTER_LEFT);

        VBox activeStat = createStatCard("🛡️", "Active Boxes", "active");
        VBox maintenanceStat = createStatCard("🔧", "Under Maintenance", "maintenance");
        VBox offlineStat = createStatCard("⚠️", "Offline", "offline");
        VBox totalStat = createStatCard("🧮", "Total Boxes", "total");

        statsOverview.getChildren().addAll(activeStat, maintenanceStat, offlineStat, totalStat);
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
            case "maintenance" -> maintenanceStatValue = valueText;
            case "offline" -> offlineStatValue = valueText;
            case "total" -> totalStatValue = valueText;
        }

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

    private void updatePoliceBoxList() {
        policeBoxList.getChildren().clear();

        if (filteredPoliceBoxes.isEmpty()) {
            Label noDataLabel = new Label("No police stations found for the selected criteria.");
            noDataLabel.setFont(Font.font("Segoe UI", 14));
            noDataLabel.setTextFill(Color.valueOf("#7f8c8d"));
            policeBoxList.getChildren().add(noDataLabel);
            return;
        }

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

        Circle boxIcon = new Circle(20);
        boxIcon.setFill(Color.valueOf("#3498db"));

        Text boxIconText = new Text("👮");
        boxIconText.setFont(Font.font(12));
        boxIconText.setFill(Color.WHITE);

        StackPane iconPane = new StackPane(boxIcon, boxIconText);

        VBox boxDetails = new VBox(4);
        HBox basicInfo = new HBox(10);
        basicInfo.setAlignment(Pos.CENTER_LEFT);

        Text boxId = new Text("ID: " + policeBox.getId());
        boxId.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        boxId.setFill(Color.valueOf("#7f8c8d"));

        Text boxName = new Text(policeBox.getName());
        boxName.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        boxName.setFill(Color.valueOf("#2c3e50"));

        basicInfo.getChildren().addAll(boxId);

        Text boxLocation = new Text(policeBox.getLocation());
        boxLocation.setFont(Font.font("Segoe UI", 12));
        boxLocation.setFill(Color.valueOf("#7f8c8d"));

        boxDetails.getChildren().addAll(boxName, basicInfo, boxLocation);

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
        // TODO: Implement detailed view
    }

    private void editPoliceBox(PoliceBox policeBox) {
        System.out.println("Editing police box: " + policeBox.getName());
        // TODO: Implement edit functionality
    }

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

        public String getId() { return id; }
        public String getName() { return name; }
        public String getLocation() { return location; }
        public String getStatus() { return status; }
        public String getDistrict() { return district; }
    }

    private void handleNavigation(String destination) {
        System.out.println("Navigating to: " + destination);
        mainController.showDashboardView();
    }
}