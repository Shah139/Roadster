package com.roadster.views;

import com.roadster.components.SideBar;
import com.roadster.controllers.MainController;
import com.roadster.service.ApiService;
import com.roadster.utils.UserManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.util.List;
import java.util.Map;

public class DashboardView extends HBox {

    public static int selectedCityIndex = 0;
    public static ComboBox<String> cityDropdown;
    private VBox sidebar;
    private VBox mainContent;
    private TextField searchField;
    private Button profileButton;
    private PieChart trafficChart;
    private VBox crimeRateStatsCard;
    private VBox trafficStatsCard;
    private VBox accidentStatsCard;
    private VBox areaCongestionCard;
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
        cityDropdown.getItems().addAll("All Districts", "Chattogram", "Dhaka", "Rajshahi", "Khulna", "Sylhet", "Barisal", "Rangpur", "Mymensingh");

        // Set default value to first item instead of placeholder text
        if (selectedCityIndex == 0 && cityDropdown.getValue() == null) {
            cityDropdown.setValue("All Districts");
            selectedCityIndex = 0;
        } else if (selectedCityIndex < cityDropdown.getItems().size()) {
            cityDropdown.setValue(cityDropdown.getItems().get(selectedCityIndex));
        }

        // Charts
        setupCharts();
    }

    private void setupCharts() {
        // Crime Data Pie Chart from API
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        
        try {
            // Fetch crime data from API
            List<Map<String, Object>> crimeData = ApiService.fetchCrimeData();
            
            if (crimeData == null || crimeData.isEmpty()) {
                throw new Exception("No crime data available");
            }
            
            // Convert API data to pie chart data
            for (Map<String, Object> crime : crimeData) {
                String district = (String) crime.get("district");
                Object countObj = crime.get("count");
                
                // Handle both String and Number types for count
                Integer count = null;
                if (countObj instanceof Number) {
                    count = ((Number) countObj).intValue();
                } else if (countObj instanceof String) {
                    try {
                        count = Integer.parseInt((String) countObj);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid count format: " + countObj);
                        continue;
                    }
                }
                
                if (district != null && count != null) {
                    pieChartData.add(new PieChart.Data(district, count));
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error loading crime data: " + e.getMessage());
            // Fallback data if API fails
            pieChartData.addAll(
                new PieChart.Data("Chattogram", 2),
                new PieChart.Data("Dhaka", 7),
                new PieChart.Data("Sylhet", 1),
                new PieChart.Data("Khulna", 3),
                new PieChart.Data("Rajshahi", 2)
            );
        }

        trafficChart = new PieChart(pieChartData);
        trafficChart.setTitle("Crime Reports by District");
        trafficChart.setLabelsVisible(true);
        trafficChart.setLegendVisible(true);
        trafficChart.setPrefSize(320, 260); // Smaller size to fit compact container
        trafficChart.setMinSize(280, 220); // Smaller minimum size
        trafficChart.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-width: 1px;");

        // Apply colors after chart is added to scene (defer styling)
        javafx.application.Platform.runLater(() -> {
            String[] colors = {"#e74c3c", "#3498db", "#2ecc71", "#f39c12", "#9b59b6", "#1abc9c"};
            int colorIndex = 0;
            for (PieChart.Data data : pieChartData) {
                if (colorIndex < colors.length && data.getNode() != null) {
                    data.getNode().setStyle("-fx-pie-color: " + colors[colorIndex] + ";");
                    colorIndex++;
                }
            }
        });

        // Initialize crime rate stats card (will be created in layout)
        crimeRateStatsCard = createCrimeRateStatsCard();
        
        // Initialize area congestion stats card
        areaCongestionCard = createAreaCongestionCard();
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

        // City dropdown for district filtering
        Label cityLabel = new Label("District:");
        cityLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        cityLabel.setTextFill(Color.valueOf("#2c3e50"));

        cityDropdown.setPrefWidth(150);
        cityDropdown.getStyleClass().add("city-dropdown");

        searchSection.getChildren().addAll(cityLabel, cityDropdown);

        // Header right section
        HBox headerRight = new HBox(15);
        headerRight.setAlignment(Pos.CENTER_RIGHT);

        // Profile button with user's name from UserManager
        String currentUserName = UserManager.getCurrentUserFullName();
        profileButton = new Button(currentUserName);
        profileButton.getStyleClass().add("profile-btn");

        headerRight.getChildren().addAll(profileButton);

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
        trafficStatsCard = createTrafficStatsCard();
        grid.add(trafficStatsCard, 1, 0);
        accidentStatsCard = createAccidentStatsCard();
        grid.add(accidentStatsCard, 1, 1);
        grid.add(crimeRateStatsCard, 0, 2);
        grid.add(areaCongestionCard, 1, 2);
        grid.add(createNavigationCard("Drivers List", "🚗", "#27ae60"), 0, 3);
        grid.add(createPoliceBoxCard(), 1, 3); // Added Police Box card
        grid.add(createReportBoxCard(), 0, 4, 2, 1); // Spans 2 columns

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

        try {
            // Get selected district from combo box
            String selectedDistrict = cityDropdown.getValue();
            if (selectedDistrict == null || selectedDistrict.equals("All Districts")) {
                selectedDistrict = "Dhaka"; // Default to Dhaka if All Districts is selected
            }

            // Fetch traffic data from API
            List<Map<String, Object>> trafficData = ApiService.fetchTrafficByDistrict(selectedDistrict);

            // Create stat items from API data
            for (Map<String, Object> traffic : trafficData) {
                String trafficLevel = (String) traffic.get("trafficLevel");
                Object countObj = traffic.get("areaCount");
                
                String count = "";
                if (countObj instanceof Number) {
                    count = String.valueOf(((Number) countObj).intValue());
                } else if (countObj instanceof String) {
                    count = (String) countObj;
                }

                String icon = getTrafficIcon(trafficLevel);
                String label = trafficLevel != null ? trafficLevel : "Unknown";
                String value = count + " areas";
                String sublabel = "Current Status";

                HBox trafficItem = createStatItem(icon, label, value, sublabel);
                statsContainer.getChildren().add(trafficItem);
            }

        } catch (Exception e) {
            System.err.println("Error loading traffic statistics: " + e.getMessage());
            // Fallback static items
            HBox highTrafficItem = createStatItem("🚦", "High Traffic", "6 areas", "Current Status");
            HBox mediumTrafficItem = createStatItem("⚠️", "Medium Traffic", "7 areas", "Current Status");
            HBox lowTrafficItem = createStatItem("✅", "Low Traffic", "3 areas", "Current Status");
            
            statsContainer.getChildren().addAll(highTrafficItem, mediumTrafficItem, lowTrafficItem);
        }

        card.getChildren().addAll(title, statsContainer);
        return card;
    }

    private String getTrafficIcon(String trafficLevel) {
        if (trafficLevel == null) return "🚦";
        
        switch (trafficLevel.toLowerCase()) {
            case "high traffic":
                return "🚦";
            case "medium traffic":
                return "⚠️";
            case "low traffic":
                return "✅";
            default:
                return "🚦";
        }
    }

    private VBox createAccidentStatsCard() {
        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.getStyleClass().add("stat-card");
        card.setPrefWidth(300);
        card.setPrefHeight(200);

        Text title = new Text("Accident Reports");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        title.setFill(Color.valueOf("#2c3e50"));

        VBox statsContainer = new VBox(15);

        try {
            // Get selected district from combo box
            String selectedDistrict = cityDropdown.getValue();
            if (selectedDistrict == null || selectedDistrict.equals("All Districts")) {
                selectedDistrict = "Dhaka"; // Default to Dhaka if All Districts is selected
            }

            // Fetch reports data from API
            List<Map<String, Object>> reportsData = ApiService.fetchReportsByDistrict(selectedDistrict);

            // Create stat items from API data
            for (Map<String, Object> report : reportsData) {
                String locationName = (String) report.get("name");
                Object countObj = report.get("count");
                
                String count = "";
                if (countObj instanceof Number) {
                    count = String.valueOf(((Number) countObj).intValue());
                } else if (countObj instanceof String) {
                    count = (String) countObj;
                }

                String icon = "🚨"; // Emergency icon for all reports
                String label = locationName != null ? locationName : "Unknown Location";
                String value = count + " report" + (Integer.parseInt(count) != 1 ? "s" : "");
                String sublabel = "Current Status";

                HBox reportItem = createStatItem(icon, label, value, sublabel);
                statsContainer.getChildren().add(reportItem);
            }

        } catch (Exception e) {
            System.err.println("Error loading accident reports: " + e.getMessage());
            // Fallback static items
            HBox gecItem = createStatItem("🚨", "GEC Circle Intersection", "1 report", "Current Status");
            HBox panchlaishItem = createStatItem("🚨", "Panchlaish Model Thana", "1 report", "Current Status");
            HBox kotwaliItem = createStatItem("🚨", "Kotwali Thana", "1 report", "Current Status");
            
            statsContainer.getChildren().addAll(gecItem, panchlaishItem, kotwaliItem);
        }

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

        // Profile button
        profileButton.setOnAction(e -> handleProfileClick());
    }

    private void handleNavigation(String destination) {
        System.out.println("Navigating to: " + destination);
        // TODO: Implement navigation to other views
        if ("drivers-list".equals(destination)) {
            mainController.showDriversView();
        } else if ("police-box".equals(destination)) {
            mainController.showPoliceBoxView();
        }
    }

    private void handleSearch() {
        String searchTerm = searchField.getText();
        System.out.println("Searching for: " + searchTerm);
        // TODO: Implement search functionality
    }

    private void handleProfileClick() {
        System.out.println("Profile button clicked");
        mainController.showUserProfileView();
    }

    private void handleCityChange() {
        String selectedCity = cityDropdown.getValue();
        System.out.println("City changed to: " + selectedCity);
        selectedCityIndex = cityDropdown.getSelectionModel().getSelectedIndex();
        
        // Refresh traffic, accident stats cards, crime rate stats card, and area congestion card with new district data
        refreshTrafficStatsCard();
        refreshAccidentStatsCard();
        refreshCrimeRateStatsCard();
        refreshAreaCongestionCard();
    }

    private void refreshTrafficStatsCard() {
        if (trafficStatsCard != null) {
            // Clear existing content except title
            trafficStatsCard.getChildren().clear();
            
            // Re-add title
            Text title = new Text("Traffic Statistics");
            title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
            title.setFill(Color.valueOf("#2c3e50"));
            
            VBox statsContainer = new VBox(15);
            
            try {
                // Get selected district from combo box
                String selectedDistrict = cityDropdown.getValue();
                if (selectedDistrict == null || selectedDistrict.equals("All Districts")) {
                    selectedDistrict = "Dhaka"; // Default to Dhaka if All Districts is selected
                }

                // Fetch traffic data from API
                List<Map<String, Object>> trafficData = ApiService.fetchTrafficByDistrict(selectedDistrict);

                // Create stat items from API data
                for (Map<String, Object> traffic : trafficData) {
                    String trafficLevel = (String) traffic.get("trafficLevel");
                    Object countObj = traffic.get("areaCount");
                    
                    String count = "";
                    if (countObj instanceof Number) {
                        count = String.valueOf(((Number) countObj).intValue());
                    } else if (countObj instanceof String) {
                        count = (String) countObj;
                    }

                    String icon = getTrafficIcon(trafficLevel);
                    String label = trafficLevel != null ? trafficLevel : "Unknown";
                    String value = count + " areas";
                    String sublabel = "Current Status";

                    HBox trafficItem = createStatItem(icon, label, value, sublabel);
                    statsContainer.getChildren().add(trafficItem);
                }

            } catch (Exception e) {
                System.err.println("Error loading traffic statistics: " + e.getMessage());
                // Fallback static items
                HBox highTrafficItem = createStatItem("🚦", "High Traffic", "6 areas", "Current Status");
                HBox mediumTrafficItem = createStatItem("⚠️", "Medium Traffic", "7 areas", "Current Status");
                HBox lowTrafficItem = createStatItem("✅", "Low Traffic", "3 areas", "Current Status");
                
                statsContainer.getChildren().addAll(highTrafficItem, mediumTrafficItem, lowTrafficItem);
            }
            
            trafficStatsCard.getChildren().addAll(title, statsContainer);
        }
    }

    private void refreshAccidentStatsCard() {
        if (accidentStatsCard != null) {
            // Clear existing content
            accidentStatsCard.getChildren().clear();
            
            // Re-add title
            Text title = new Text("Accident Reports");
            title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
            title.setFill(Color.valueOf("#2c3e50"));
            
            VBox statsContainer = new VBox(15);
            
            try {
                // Get selected district from combo box
                String selectedDistrict = cityDropdown.getValue();
                if (selectedDistrict == null || selectedDistrict.equals("All Districts")) {
                    selectedDistrict = "Dhaka"; // Default to Dhaka if All Districts is selected
                }

                // Fetch reports data from API
                List<Map<String, Object>> reportsData = ApiService.fetchReportsByDistrict(selectedDistrict);

                // Create stat items from API data
                for (Map<String, Object> report : reportsData) {
                    String locationName = (String) report.get("name");
                    Object countObj = report.get("count");
                    
                    String count = "";
                    if (countObj instanceof Number) {
                        count = String.valueOf(((Number) countObj).intValue());
                    } else if (countObj instanceof String) {
                        count = (String) countObj;
                    }

                    String icon = "🚨"; // Emergency icon for all reports
                    String label = locationName != null ? locationName : "Unknown Location";
                    String value = count + " report" + (Integer.parseInt(count) != 1 ? "s" : "");
                    String sublabel = "Current Status";

                    HBox reportItem = createStatItem(icon, label, value, sublabel);
                    statsContainer.getChildren().add(reportItem);
                }

            } catch (Exception e) {
                System.err.println("Error loading accident reports: " + e.getMessage());
                // Fallback static items
                HBox gecItem = createStatItem("🚨", "GEC Circle Intersection", "1 report", "Current Status");
                HBox panchlaishItem = createStatItem("🚨", "Panchlaish Model Thana", "1 report", "Current Status");
                HBox kotwaliItem = createStatItem("🚨", "Kotwali Thana", "1 report", "Current Status");
                
                statsContainer.getChildren().addAll(gecItem, panchlaishItem, kotwaliItem);
            }
            
            accidentStatsCard.getChildren().addAll(title, statsContainer);
        }
    }

    private VBox createCrimeRateStatsCard() {
        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.getStyleClass().add("stat-card");
        card.setPrefWidth(300);

        Text title = new Text("Area Crime Rates");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        title.setFill(Color.valueOf("#2c3e50"));
        
        VBox statsContainer = new VBox(15);
        
        try {
            // Get selected district from combo box
            String selectedDistrict = cityDropdown != null ? cityDropdown.getValue() : "Dhaka";
            if (selectedDistrict == null || selectedDistrict.equals("All Districts")) {
                selectedDistrict = "Dhaka"; // Default to Dhaka if All Districts is selected
            }

            // Fetch crime rate data from API
            List<Map<String, Object>> crimeRateData = ApiService.fetchAreaCrimeRatesByDistrict(selectedDistrict);

            // Create stat items from API data (limit to first 8 items for better display)
            int count = 0;
            for (Map<String, Object> crimeRate : crimeRateData) {
                if (count >= 8) break; // Limit to 8 areas for better card layout
                
                String areaName = (String) crimeRate.get("name");
                Object rateObj = crimeRate.get("crimeRate");
                
                // Handle both Double and String types for crime rate
                Double rate = null;
                if (rateObj instanceof Number) {
                    rate = ((Number) rateObj).doubleValue();
                } else if (rateObj instanceof String) {
                    try {
                        rate = Double.parseDouble((String) rateObj);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid crime rate format: " + rateObj);
                        continue;
                    }
                }
                
                if (areaName != null && rate != null) {
                    String rateDisplay = String.format("%.3f", rate);
                    String icon = getCrimeRateIcon(rate);
                    HBox crimeRateItem = createStatItem(icon, areaName, rateDisplay, "Crime Rate");
                    statsContainer.getChildren().add(crimeRateItem);
                    count++;
                }
            }

            System.out.println("Loaded " + count + " crime rate data points for district: " + selectedDistrict);

        } catch (Exception e) {
            System.err.println("Error loading crime rate data: " + e.getMessage());
            
            // Fallback data if API fails
            HBox mirpurItem = createStatItem("🔴", "Mirpur", "0.150", "Crime Rate");
            HBox gulshanItem = createStatItem("🟢", "Gulshan", "0.050", "Crime Rate");
            HBox dhanmondiItem = createStatItem("🟠", "Dhanmondi", "0.140", "Crime Rate");
            HBox tejgaonItem = createStatItem("🔴", "Tejgaon", "0.180", "Crime Rate");
            HBox uttaraItem = createStatItem("🟠", "Uttara", "0.100", "Crime Rate");
            HBox baddaItem = createStatItem("🟠", "Badda", "0.100", "Crime Rate");
            
            statsContainer.getChildren().addAll(mirpurItem, gulshanItem, dhanmondiItem, tejgaonItem, uttaraItem, baddaItem);
        }
        
        card.getChildren().addAll(title, statsContainer);
        return card;
    }

    private String getCrimeRateIcon(double rate) {
        if (rate <= 0.05) {
            return "🟢"; // Green circle for very low crime rate
        } else if (rate <= 0.10) {
            return "🟡"; // Yellow circle for low crime rate
        } else if (rate <= 0.15) {
            return "🟠"; // Orange circle for medium crime rate
        } else {
            return "🔴"; // Red circle for high crime rate
        }
    }

    private void refreshCrimeRateStatsCard() {
        if (crimeRateStatsCard != null) {
            // Clear existing content
            crimeRateStatsCard.getChildren().clear();
            
            // Re-add title
            Text title = new Text("Area Crime Rates");
            title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
            title.setFill(Color.valueOf("#2c3e50"));
            
            VBox statsContainer = new VBox(15);
            
            try {
                // Get selected district from combo box
                String selectedDistrict = cityDropdown.getValue();
                if (selectedDistrict == null || selectedDistrict.equals("All Districts")) {
                    selectedDistrict = "Dhaka"; // Default to Dhaka if All Districts is selected
                }

                // Fetch crime rate data from API
                List<Map<String, Object>> crimeRateData = ApiService.fetchAreaCrimeRatesByDistrict(selectedDistrict);

                // Create stat items from API data (limit to first 8 items for better display)
                int count = 0;
                for (Map<String, Object> crimeRate : crimeRateData) {
                    if (count >= 8) break; // Limit to 8 areas for better card layout
                    
                    String areaName = (String) crimeRate.get("name");
                    Object rateObj = crimeRate.get("crimeRate");
                    
                    // Handle both Double and String types for crime rate
                    Double rate = null;
                    if (rateObj instanceof Number) {
                        rate = ((Number) rateObj).doubleValue();
                    } else if (rateObj instanceof String) {
                        try {
                            rate = Double.parseDouble((String) rateObj);
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid crime rate format: " + rateObj);
                            continue;
                        }
                    }
                    
                    if (areaName != null && rate != null) {
                        String rateDisplay = String.format("%.3f", rate);
                        String icon = getCrimeRateIcon(rate);
                        HBox crimeRateItem = createStatItem(icon, areaName, rateDisplay, "Crime Rate");
                        statsContainer.getChildren().add(crimeRateItem);
                        count++;
                    }
                }

            } catch (Exception e) {
                System.err.println("Error loading crime rate data: " + e.getMessage());
                
                // Fallback data if API fails
                HBox mirpurItem = createStatItem("🔴", "Mirpur", "0.150", "Crime Rate");
                HBox gulshanItem = createStatItem("🟢", "Gulshan", "0.050", "Crime Rate");
                HBox dhanmondiItem = createStatItem("🟠", "Dhanmondi", "0.140", "Crime Rate");
                HBox tejgaonItem = createStatItem("🔴", "Tejgaon", "0.180", "Crime Rate");
                
                statsContainer.getChildren().addAll(mirpurItem, gulshanItem, dhanmondiItem, tejgaonItem);
            }
            
            crimeRateStatsCard.getChildren().addAll(title, statsContainer);
        }
    }



    private VBox createTrafficChartCard() {
        VBox card = new VBox(8); // Reduced spacing for more compact layout
        card.setPadding(new Insets(15)); // Reduced padding to save space
        card.getStyleClass().add("stat-card");
        card.setPrefWidth(300); // Smaller width to reduce white space
        card.setMaxWidth(Double.MAX_VALUE); // Allow expansion
        card.setMaxHeight(Double.MAX_VALUE); // Allow expansion
        card.setAlignment(Pos.CENTER); // Center content vertically

        Text title = new Text("Crime Reports by District");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16)); // Slightly smaller title
        title.setFill(Color.valueOf("#2c3e50"));

        // Chart container with explicit styling to ensure visibility
        VBox chartContainer = new VBox(5);
        chartContainer.setAlignment(Pos.CENTER);
        chartContainer.setPrefHeight(280); // Reduced height to save space
        chartContainer.setMaxHeight(Region.USE_PREF_SIZE); // Don't grow beyond preferred height
        chartContainer.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-width: 1px; -fx-border-radius: 5px;");
        
        // Make chartContainer grow to fill available space for proper centering
        VBox.setVgrow(chartContainer, Priority.ALWAYS);
        
        if (trafficChart != null) {
            chartContainer.getChildren().add(trafficChart);
        } else {
            Text errorText = new Text("Loading Crime Data Chart...");
            errorText.setFont(Font.font("Segoe UI", 14));
            errorText.setFill(Color.GRAY);
            chartContainer.getChildren().add(errorText);
        }

        // Add components to card (removed crime rate stats)
        card.getChildren().addAll(title, chartContainer);

        return card;
    }

    private VBox createAreaCongestionCard() {
        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.getStyleClass().add("stat-card");
        card.setPrefWidth(300);

        Text title = new Text("Area Congestion Levels");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        title.setFill(Color.valueOf("#2c3e50"));
        
        VBox statsContainer = new VBox(15);
        
        try {
            // Get selected district from combo box
            String selectedDistrict = cityDropdown != null ? cityDropdown.getValue() : "Dhaka";
            if (selectedDistrict == null || selectedDistrict.equals("All Districts")) {
                selectedDistrict = "Dhaka"; // Default to Dhaka if All Districts is selected
            }

            // Fetch congestion data from API
            List<Map<String, Object>> congestionData = ApiService.fetchAreaCongestionByDistrict(selectedDistrict);

            // Create stat items from API data (limit to first 8 items for better display)
            int count = 0;
            for (Map<String, Object> congestion : congestionData) {
                if (count >= 8) break; // Limit to 8 areas for better card layout
                
                String areaName = (String) congestion.get("name");
                Object levelObj = congestion.get("congestionLevel");
                
                // Handle both Double and String types for congestion level
                Double level = null;
                if (levelObj instanceof Number) {
                    level = ((Number) levelObj).doubleValue();
                } else if (levelObj instanceof String) {
                    try {
                        level = Double.parseDouble((String) levelObj);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid congestion level format: " + levelObj);
                        continue;
                    }
                }
                
                if (areaName != null && level != null) {
                    String levelDisplay = String.format("%.2f", level);
                    String icon = getCongestionIcon(level);
                    HBox congestionItem = createStatItem(icon, areaName, levelDisplay, "Congestion");
                    statsContainer.getChildren().add(congestionItem);
                    count++;
                }
            }

            System.out.println("Loaded " + count + " congestion data points for district: " + selectedDistrict);

        } catch (Exception e) {
            System.err.println("Error loading congestion data: " + e.getMessage());
            
            // Fallback data if API fails
            HBox mirpurItem = createStatItem("🔴", "Mirpur", "0.80", "Congestion");
            HBox gulshanItem = createStatItem("🟡", "Gulshan", "0.60", "Congestion");
            HBox dhanmondiItem = createStatItem("🔴", "Dhanmondi", "0.80", "Congestion");
            HBox tejgaonItem = createStatItem("🔴", "Tejgaon", "0.90", "Congestion");
            HBox uttaraItem = createStatItem("🟡", "Uttara", "0.60", "Congestion");
            HBox baddaItem = createStatItem("🟠", "Badda", "0.70", "Congestion");
            
            statsContainer.getChildren().addAll(mirpurItem, gulshanItem, dhanmondiItem, tejgaonItem, uttaraItem, baddaItem);
        }
        
        card.getChildren().addAll(title, statsContainer);
        return card;
    }

    private String getCongestionIcon(double level) {
        if (level <= 0.5) {
            return "🟢"; // Green circle for low congestion
        } else if (level <= 0.7) {
            return "🟡"; // Yellow circle for moderate congestion
        } else if (level <= 0.8) {
            return "🟠"; // Orange circle for high congestion
        } else {
            return "🔴"; // Red circle for very high congestion
        }
    }

    private void refreshAreaCongestionCard() {
        if (areaCongestionCard != null) {
            // Clear existing content
            areaCongestionCard.getChildren().clear();
            
            // Re-add title
            Text title = new Text("Area Congestion Levels");
            title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
            title.setFill(Color.valueOf("#2c3e50"));
            
            VBox statsContainer = new VBox(15);
            
            try {
                // Get selected district from combo box
                String selectedDistrict = cityDropdown.getValue();
                if (selectedDistrict == null || selectedDistrict.equals("All Districts")) {
                    selectedDistrict = "Dhaka"; // Default to Dhaka if All Districts is selected
                }

                // Fetch congestion data from API
                List<Map<String, Object>> congestionData = ApiService.fetchAreaCongestionByDistrict(selectedDistrict);

                // Create stat items from API data (limit to first 8 items for better display)
                int count = 0;
                for (Map<String, Object> congestion : congestionData) {
                    if (count >= 8) break; // Limit to 8 areas for better card layout
                    
                    String areaName = (String) congestion.get("name");
                    Object levelObj = congestion.get("congestionLevel");
                    
                    // Handle both Double and String types for congestion level
                    Double level = null;
                    if (levelObj instanceof Number) {
                        level = ((Number) levelObj).doubleValue();
                    } else if (levelObj instanceof String) {
                        try {
                            level = Double.parseDouble((String) levelObj);
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid congestion level format: " + levelObj);
                            continue;
                        }
                    }
                    
                    if (areaName != null && level != null) {
                        String levelDisplay = String.format("%.2f", level);
                        String icon = getCongestionIcon(level);
                        HBox congestionItem = createStatItem(icon, areaName, levelDisplay, "Congestion");
                        statsContainer.getChildren().add(congestionItem);
                        count++;
                    }
                }

            } catch (Exception e) {
                System.err.println("Error loading congestion data: " + e.getMessage());
                
                // Fallback data if API fails
                HBox mirpurItem = createStatItem("🔴", "Mirpur", "0.80", "Congestion");
                HBox gulshanItem = createStatItem("🟡", "Gulshan", "0.60", "Congestion");
                HBox dhanmondiItem = createStatItem("🔴", "Dhanmondi", "0.80", "Congestion");
                HBox tejgaonItem = createStatItem("🔴", "Tejgaon", "0.90", "Congestion");
                
                statsContainer.getChildren().addAll(mirpurItem, gulshanItem, dhanmondiItem, tejgaonItem);
            }
            
            areaCongestionCard.getChildren().addAll(title, statsContainer);
        }
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

    // New Police Box card method
    private VBox createPoliceBoxCard() {
        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.getStyleClass().add("stat-card");
        card.setPrefWidth(300);
        card.setPrefHeight(150);
        card.setStyle("-fx-cursor: hand;");

        HBox content = new HBox(15);
        content.setAlignment(Pos.CENTER_LEFT);

        Circle iconCircle = new Circle(25);
        iconCircle.setFill(Color.valueOf("#3f51b5")); // Police blue color

        Text iconText = new Text("👮");
        iconText.setFont(Font.font(20));

        StackPane iconPane = new StackPane(iconCircle, iconText);

        VBox info = new VBox(5);
        Text titleText = new Text("Police Box");
        titleText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        titleText.setFill(Color.valueOf("#2c3e50"));

        Text subtitleText = new Text("Emergency & Reports");
        subtitleText.setFont(Font.font("Segoe UI", 12));
        subtitleText.setFill(Color.valueOf("#7f8c8d"));

        info.getChildren().addAll(titleText, subtitleText);

        content.getChildren().addAll(iconPane, info);
        card.getChildren().add(content);

        // Add click handler
        card.setOnMouseClicked(e -> handleNavigation("police-box"));

        return card;
    }

    private VBox createReportBoxCard() {
        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.getStyleClass().add("stat-card");
        card.setPrefWidth(800);
        card.setPrefHeight(200);

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        Text title = new Text("Report an Incident");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        title.setFill(Color.valueOf("#2c3e50"));

        HBox.setHgrow(title, Priority.ALWAYS);
        header.getChildren().add(title);

        // Report content
        VBox reportContent = new VBox(15);
        reportContent.setAlignment(Pos.CENTER);

        Text reportText = new Text("� Seen something that needs attention?");
        reportText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        reportText.setFill(Color.valueOf("#3498db"));

        Text subText = new Text("Help improve road safety by reporting incidents in " + 
                               (cityDropdown.getValue() != null ? cityDropdown.getValue() : "your city"));
        subText.setFont(Font.font("Segoe UI", 12));
        subText.setFill(Color.valueOf("#7f8c8d"));
        subText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        // Create report button
        Button reportButton = new Button("Report Now");
        reportButton.setPrefWidth(200);
        reportButton.setPrefHeight(40);
        reportButton.setStyle(
            "-fx-background-color: #3498db;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-font-size: 14px;"
        );

        // Add hover effects
        reportButton.setOnMouseEntered(e -> 
            reportButton.setStyle(
                "-fx-background-color: #2980b9;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-font-size: 14px;"
            )
        );

        reportButton.setOnMouseExited(e -> 
            reportButton.setStyle(
                "-fx-background-color: #3498db;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-font-size: 14px;"
            )
        );

        // Handle button click - navigate to report view with selected city
        reportButton.setOnAction(e -> {
            String selectedCity = cityDropdown.getValue();
            if (selectedCity == null || selectedCity.equals("All Districts")) {
                selectedCity = "Dhaka"; // Default to Dhaka
            }
            System.out.println("Navigating to Report View for city: " + selectedCity);
            mainController.showReportView();
        });

        reportContent.getChildren().addAll(reportText, subText, reportButton);

        card.getChildren().addAll(header, reportContent);
        return card;
    }
}