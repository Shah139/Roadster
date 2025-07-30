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
import javafx.scene.shape.Rectangle;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
public class DashboardView extends HBox {

    private VBox sidebar;
    private VBox mainContent;
    private TextField searchField;
    private ComboBox<String> cityDropdown;
    private PieChart trafficChart;
    private LineChart<String, Number> trendChart;
    private MainController mainController;

    public DashboardView(MainController mainController) {
        this.mainController = mainController;
        initializeComponents();
        setupLayout();
        setupStyling();
        setupEventHandlers();
    }

    private void initializeComponents() {
        // Search components
        searchField = new TextField();
        searchField.setPromptText("Search");

        cityDropdown = new ComboBox<>();
        cityDropdown.getItems().addAll("Chattogram", "Dhaka", "Rajshahi", "Khulna", "Sylhet", "Barisal", "Rangpur", "Mymensingh");
        cityDropdown.setValue("City Location");

        // Charts
        setupCharts();
    }

    private void setupCharts() {
        // Traffic Pie Chart with more realistic data
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Low Traffic", 25),
                new PieChart.Data("Medium Traffic", 40),
                new PieChart.Data("High Traffic", 35)
        );

        trafficChart = new PieChart(pieChartData);
        trafficChart.setTitle("Traffic Distribution");
        trafficChart.setLabelsVisible(true);
        trafficChart.setLegendVisible(true);
        // Remove setPrefSize(300, 200) to allow dynamic scaling
        trafficChart.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // Allow chart to scale

        // Style the pie chart
        for (PieChart.Data data : pieChartData) {
            if (data.getName().equals("Low Traffic")) {
                data.getNode().setStyle("-fx-pie-color: #27ae60;");
            } else if (data.getName().equals("Medium Traffic")) {
                data.getNode().setStyle("-fx-pie-color: #f39c12;");
            } else if (data.getName().equals("High Traffic")) {
                data.getNode().setStyle("-fx-pie-color: #e74c3c;");
            }
        }

        // Trend Line Chart with more realistic data
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis(0, 100, 10);
        xAxis.setLabel("Time");
        yAxis.setLabel("Traffic Level (%)");

        trendChart = new LineChart<>(xAxis, yAxis);
        trendChart.setTitle("Live Traffic Alert");
        trendChart.setPrefSize(400, 250);

        // Add multiple series for different metrics
        XYChart.Series<String, Number> trafficSeries = new XYChart.Series<>();
        trafficSeries.setName("Traffic Congestion");
        trafficSeries.getData().add(new XYChart.Data<>("6 AM", 15));
        trafficSeries.getData().add(new XYChart.Data<>("9 AM", 85));
        trafficSeries.getData().add(new XYChart.Data<>("12 PM", 65));
        trafficSeries.getData().add(new XYChart.Data<>("3 PM", 75));
        trafficSeries.getData().add(new XYChart.Data<>("6 PM", 95));
        trafficSeries.getData().add(new XYChart.Data<>("9 PM", 45));

        XYChart.Series<String, Number> accidentSeries = new XYChart.Series<>();
        accidentSeries.setName("Accident Density");
        accidentSeries.getData().add(new XYChart.Data<>("6 AM", 5));
        accidentSeries.getData().add(new XYChart.Data<>("9 AM", 25));
        accidentSeries.getData().add(new XYChart.Data<>("12 PM", 15));
        accidentSeries.getData().add(new XYChart.Data<>("3 PM", 20));
        accidentSeries.getData().add(new XYChart.Data<>("6 PM", 35));
        accidentSeries.getData().add(new XYChart.Data<>("9 PM", 10));

        trendChart.getData().addAll(trafficSeries, accidentSeries);

        // Style the line chart
        trendChart.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 1;");
    }
    private void setupLayout() {
        setSpacing(0);

        // Sidebar
        SideBar sideBar = new SideBar(mainController);
        sidebar = sideBar.createSidebar();
        // Main content
        mainContent = createMainContent();

        getChildren().addAll(sidebar, mainContent);
        HBox.setHgrow(sidebar, Priority.NEVER); // Sidebar stays fixed width
        HBox.setHgrow(mainContent, Priority.ALWAYS); // mainContent expands to fill remaining space
    }


    private VBox createMainContent() {
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));
        mainContent.getStyleClass().add("main-content");

        // Header
        HBox header = createHeader();

        // Dashboard content
        VBox dashboardContent = createDashboardContent();

        mainContent.getChildren().addAll(header, dashboardContent);
        VBox.setVgrow(dashboardContent, Priority.ALWAYS); // Ensure dashboardContent expands vertically
        return mainContent;
    }

    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));

        // Search section
        HBox searchSection = new HBox(10);
        searchSection.setAlignment(Pos.CENTER_LEFT);

        // City dropdown
        Label cityLabel = new Label("City Location:");
        cityLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));

        cityDropdown.setPrefWidth(150);

        // Search input
        HBox searchContainer = new HBox(8);
        searchContainer.setAlignment(Pos.CENTER_LEFT);
        searchContainer.getStyleClass().add("search-container");

        Text searchIcon = new Text("🔍");
        searchIcon.setFont(Font.font(14));

        searchField.setPrefWidth(300);
        searchField.setPrefHeight(35);

        searchContainer.getChildren().addAll(searchIcon, searchField);

        searchSection.getChildren().addAll(cityLabel, cityDropdown, searchContainer);

        // Header right section
        HBox headerRight = new HBox(15);
        headerRight.setAlignment(Pos.CENTER_RIGHT);

        Button profileButton = new Button("Profile Name");
        profileButton.getStyleClass().add("profile-btn");

        // User profile
        HBox userProfile = new HBox(10);
        userProfile.setAlignment(Pos.CENTER);

        Circle userAvatar = new Circle(20);
        userAvatar.setFill(Color.valueOf("#3498db"));

        VBox userInfo = new VBox(2);
        Text userName = new Text("City Name");
        userName.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        Text userRole = new Text("Profile Name");
        userRole.setFont(Font.font("Segoe UI", 10));
        userRole.setFill(Color.valueOf("#7f8c8d"));

        userInfo.getChildren().addAll(userName, userRole);
        userProfile.getChildren().addAll(userAvatar, userInfo);

        headerRight.getChildren().addAll(profileButton, userProfile);

        header.getChildren().addAll(searchSection, headerRight);
        HBox.setHgrow(searchSection, Priority.ALWAYS);

        return header;
    }

    private VBox createDashboardContent() {
        // Create GridPane for cards
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(20));

        // Set column constraints
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        col1.setHgrow(Priority.ALWAYS);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        col2.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().addAll(col1, col2);

        // Set row constraints to allow dynamic height
        RowConstraints row1 = new RowConstraints();
        row1.setVgrow(Priority.ALWAYS);
        grid.getRowConstraints().add(row1); // Apply to first row, adjust as needed

        // Add cards to grid with row and column constraints
        grid.add(createTrafficChartCard(), 0, 0, 1, 2); // Spans 2 rows
        grid.add(createTrafficStatsCard(), 1, 0);
        grid.add(createAccidentStatsCard(), 1, 1);
        grid.add(createStatCard("Live Traffic Alert", trendChart), 0, 2);
        grid.add(createIncidentAnalysisCard(), 1, 2);
        grid.add(createNavigationCard("Police Box", "👮", "#e74c3c"), 0, 3);
        grid.add(createNavigationCard("Drivers List", "🚗", "#27ae60"), 1, 3);
        grid.add(createHighlightCard(), 0, 4, 2, 1); // Spans 2 columns

        // Wrap grid in a ScrollPane
        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        // Ensure the ScrollPane expands to fill available space
        VBox content = new VBox(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        return content;
    }

    private VBox createStatCard(String title, Node content) {
        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.getStyleClass().add("stat-card");
        card.setPrefWidth(400);
        card.setPrefHeight(300);

        Text titleText = new Text(title);
        titleText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        titleText.setFill(Color.valueOf("#2c3e50"));

        card.getChildren().addAll(titleText, content);
        return card;
    }

    private VBox createTrafficStatsCard() {
        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.getStyleClass().add("stat-card");
        card.setPrefWidth(300);
        card.setPrefHeight(200);

        Text title = new Text("Traffic Statistics");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        title.setFill(Color.valueOf("#2c3e50"));

        VBox statsContainer = new VBox(15);

        // Traffic Congestion
        HBox trafficItem = createStatItem("🚦", "Traffic Congestion", "48%", "High");

        // Accident Rate
        HBox accidentItem = createStatItem("⚠️", "Accident Rate", "12%", "Low");

        // Response Time
        HBox responseItem = createStatItem("⏱️", "Response Time", "3.2 min", "Good");

        statsContainer.getChildren().addAll(trafficItem, accidentItem, responseItem);
        card.getChildren().addAll(title, statsContainer);

        return card;
    }

    private VBox createAccidentStatsCard() {
        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.getStyleClass().add("stat-card");
        card.setPrefWidth(300);
        card.setPrefHeight(200);

        Text title = new Text("Accident Statistics");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        title.setFill(Color.valueOf("#2c3e50"));

        VBox statsContainer = new VBox(15);

        // Total Accidents
        HBox totalItem = createStatItem("📊", "Total Accidents", "156", "This Month");

        // Fatal Accidents
        HBox fatalItem = createStatItem("💀", "Fatal Accidents", "8", "This Month");

        // Injury Accidents
        HBox injuryItem = createStatItem("🏥", "Injury Accidents", "45", "This Month");

        statsContainer.getChildren().addAll(totalItem, fatalItem, injuryItem);
        card.getChildren().addAll(title, statsContainer);

        return card;
    }

    private HBox createStatItem(String icon, String label, String value, String sublabel) {
        HBox item = new HBox(12);
        item.setAlignment(Pos.CENTER_LEFT);

        Text iconText = new Text(icon);
        iconText.setFont(Font.font(20));

        VBox info = new VBox(2);
        Text labelText = new Text(label);
        labelText.setFont(Font.font("Segoe UI", 12));
        labelText.setFill(Color.valueOf("#7f8c8d"));

        Text valueText = new Text(value);
        valueText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        valueText.setFill(Color.valueOf("#2c3e50"));

        Text sublabelText = new Text(sublabel);
        sublabelText.setFont(Font.font("Segoe UI", 10));
        sublabelText.setFill(Color.valueOf("#95a5a6"));

        info.getChildren().addAll(labelText, valueText, sublabelText);
        item.getChildren().addAll(iconText, info);

        return item;
    }

    private void setupStyling() {
        getStyleClass().add("dashboard-view");
    }

    private void setupEventHandlers() {
        // Search functionality
        searchField.setOnAction(e -> handleSearch());

        // City dropdown change
        cityDropdown.setOnAction(e -> handleCityChange());
    }

    private void handleNavigation(String destination) {
        System.out.println("Navigating to: " + destination);
        // TODO: Implement navigation to other views
        if(destination == "Dashboard"){
            mainController.showDashboardView();
        }else if(destination == "User Profile"){
            mainController.showUserProfileView();
        }

    }

    private void handleSearch() {
        String searchTerm = searchField.getText();
        System.out.println("Searching for: " + searchTerm);
        // TODO: Implement search functionality
    }

    private void handleCityChange() {
        String selectedCity = cityDropdown.getValue();
        System.out.println("City changed to: " + selectedCity);
        // TODO: Update dashboard data based on selected city
    }

    private VBox createTrafficChartCard() {
        VBox card = new VBox(10); // Reduced spacing for tighter layout
        card.setPadding(new Insets(20));
        card.getStyleClass().add("stat-card");
        card.setPrefWidth(400); // Keep preferred width
        card.setMaxWidth(Double.MAX_VALUE); // Allow expansion
        card.setMaxHeight(Double.MAX_VALUE); // Allow expansion
        card.setAlignment(Pos.TOP_LEFT); // Push content to the top

        Text title = new Text("Traffic Chart");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setFill(Color.valueOf("#2c3e50"));

        // Chart container
        VBox chartContainer = new VBox(5);
        chartContainer.getChildren().add(trafficChart);
        // No fixed prefSize, let it scale with parent

        // Crime rate stats
        HBox crimeStats = new HBox(15);
        crimeStats.setAlignment(Pos.CENTER_LEFT);

        Circle crimeIcon = new Circle(8);
        crimeIcon.setFill(Color.valueOf("#e74c3c"));

        VBox crimeInfo = new VBox(2);
        Text crimeLabel = new Text("Crime Rate");
        crimeLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        crimeLabel.setFill(Color.valueOf("#2c3e50"));

        Text crimeValue = new Text("30%");
        crimeValue.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        crimeValue.setFill(Color.valueOf("#e74c3c"));

        Text crimeSub = new Text("Summer Zone");
        crimeSub.setFont(Font.font("Segoe UI", 10));
        crimeSub.setFill(Color.valueOf("#7f8c8d"));

        crimeInfo.getChildren().addAll(crimeLabel, crimeValue, crimeSub);
        crimeStats.getChildren().addAll(crimeIcon, crimeInfo);

        // Add components to card
        card.getChildren().addAll(title, chartContainer, crimeStats);

        return card;
    }

    private VBox createIncidentAnalysisCard() {
        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.getStyleClass().add("stat-card");
        card.setPrefWidth(300);
        card.setPrefHeight(200);

        Text title = new Text("Incident Analysis");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        title.setFill(Color.valueOf("#2c3e50"));

        // Percentage stats
        HBox percentageStats = new HBox(20);
        percentageStats.setAlignment(Pos.CENTER);

        VBox morningStats = new VBox(5);
        morningStats.setAlignment(Pos.CENTER);
        Text morningValue = new Text("25%");
        morningValue.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        morningValue.setFill(Color.valueOf("#3498db"));
        Text morningLabel = new Text("Morning");
        morningLabel.setFont(Font.font("Segoe UI", 12));
        morningLabel.setFill(Color.valueOf("#7f8c8d"));
        morningStats.getChildren().addAll(morningValue, morningLabel);

        VBox eveningStats = new VBox(5);
        eveningStats.setAlignment(Pos.CENTER);
        Text eveningValue = new Text("35%");
        eveningValue.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        eveningValue.setFill(Color.valueOf("#e74c3c"));
        Text eveningLabel = new Text("Evening");
        eveningLabel.setFont(Font.font("Segoe UI", 12));
        eveningLabel.setFill(Color.valueOf("#7f8c8d"));
        eveningStats.getChildren().addAll(eveningValue, eveningLabel);

        percentageStats.getChildren().addAll(morningStats, eveningStats);

        // Simple bar representation
        HBox barContainer = new HBox(10);
        barContainer.setAlignment(Pos.CENTER);

        Rectangle morningBar = new Rectangle(60, 20);
        morningBar.setFill(Color.valueOf("#3498db"));
        morningBar.setArcWidth(5);
        morningBar.setArcHeight(5);

        Rectangle eveningBar = new Rectangle(84, 20);
        eveningBar.setFill(Color.valueOf("#e74c3c"));
        eveningBar.setArcWidth(5);
        eveningBar.setArcHeight(5);

        barContainer.getChildren().addAll(morningBar, eveningBar);

        card.getChildren().addAll(title, percentageStats, barContainer);
        return card;
    }

    private VBox createNavigationCard(String title, String icon, String color) {
        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.getStyleClass().add("stat-card");
        card.setPrefWidth(300);
        card.setPrefHeight(150);
        card.setStyle("-fx-cursor: hand;");

        HBox content = new HBox(15);
        content.setAlignment(Pos.CENTER_LEFT);

        Circle iconCircle = new Circle(25);
        iconCircle.setFill(Color.valueOf(color));

        Text iconText = new Text(icon);
        iconText.setFont(Font.font(20));
        iconText.setFill(Color.WHITE);

        StackPane iconPane = new StackPane(iconCircle, iconText);

        VBox info = new VBox(5);
        Text titleText = new Text(title);
        titleText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        titleText.setFill(Color.valueOf("#2c3e50"));

        info.getChildren().add(titleText);

        content.getChildren().addAll(iconPane, info);
        card.getChildren().add(content);

        // Add click handler
        card.setOnMouseClicked(e -> handleNavigation(title.toLowerCase().replace(" ", "-")));

        return card;
    }

    private VBox createHighlightCard() {
        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.getStyleClass().add("stat-card");
        card.setPrefWidth(800);
        card.setPrefHeight(200);

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        Text title = new Text("Highlight");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        title.setFill(Color.valueOf("#2c3e50"));

        HBox.setHgrow(title, Priority.ALWAYS);
        header.getChildren().add(title);

        // Highlight content
        VBox highlightContent = new VBox(10);
        highlightContent.setAlignment(Pos.CENTER);

        Text highlightText = new Text("🚨 Emergency Response Time: 2.3 minutes");
        highlightText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        highlightText.setFill(Color.valueOf("#27ae60"));

        Text subText = new Text("All systems operational • Last updated: 2 minutes ago");
        subText.setFont(Font.font("Segoe UI", 12));
        subText.setFill(Color.valueOf("#7f8c8d"));

        highlightContent.getChildren().addAll(highlightText, subText);

        card.getChildren().addAll(header, highlightContent);
        return card;
    }
}