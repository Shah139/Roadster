package com.roadster.views;

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
import javafx.scene.shape.Polygon;
import javafx.scene.Group;
public class MapsView extends VBox {
    private MainController mainController;
    private HBox header;
    private HBox mainContent;
    private VBox sidebar;
    private Pane mapContainer;
    private TextField searchField;
    private VBox layerControls;
    private Pane mapLegend;
    private Pane aiAssistant;
    
    // Layer toggles
    private ToggleButton crimeZonesToggle;
    private ToggleButton listenToggle;
    private ToggleButton cctvToggle;
    private ToggleButton congestedRoadsToggle;
    
    // Map elements
    private Group mapElements;
    private Pane roadDetailsModal;
    
    public MapsView(MainController mainController) {
        this.mainController = mainController;
        initializeComponents();
        setupLayout();
        setupStyling();
        setupEventHandlers();
        createMapElements();
    }
    
    private void initializeComponents() {
        // Search field
        searchField = new TextField();
        searchField.setPromptText("Search location");

        // Layer toggles
        crimeZonesToggle = new ToggleButton();
        crimeZonesToggle.setSelected(true);
        crimeZonesToggle.getStyleClass().add("toggle-switch");
        
        listenToggle = new ToggleButton();
        listenToggle.getStyleClass().add("toggle-switch");
        
        cctvToggle = new ToggleButton();
        cctvToggle.setSelected(true);
        cctvToggle.getStyleClass().add("toggle-switch");
        
        congestedRoadsToggle = new ToggleButton();
        congestedRoadsToggle.getStyleClass().add("toggle-switch");
        
        // Map container
        mapContainer = new Pane();
        mapContainer.getStyleClass().add("map-container");
        
        // Map elements group
        mapElements = new Group();
        
        // Layer controls
        layerControls = new VBox(15);
        layerControls.setPadding(new Insets(20));
        layerControls.getStyleClass().add("sidebar");
        
        // Map legend
        mapLegend = new VBox(10);
        mapLegend.setPadding(new Insets(15));
        mapLegend.getStyleClass().add("map-legend");
        
        // AI Assistant
        aiAssistant = new Pane();
        aiAssistant.getStyleClass().add("ai-assistant");
        
        // Road details modal
        roadDetailsModal = new VBox(15);
        roadDetailsModal.setPadding(new Insets(20));
        roadDetailsModal.getStyleClass().add("road-details-modal");
        roadDetailsModal.setVisible(false);
    }
    
    private void setupLayout() {
        setSpacing(0);
        
        // Header
        header = createHeader();
        
        // Main content
        mainContent = new HBox(0);
        
        // Sidebar
        sidebar = createSidebar();
        
        // Map container with legend
        VBox mapSection = new VBox(0);
        mapSection.getChildren().addAll(mapContainer, mapLegend);
        VBox.setVgrow(mapContainer, Priority.ALWAYS);
        
        mainContent.getChildren().addAll(sidebar, mapSection);
        HBox.setHgrow(sidebar, Priority.NEVER);
        HBox.setHgrow(mapSection, Priority.ALWAYS);
        
        // Add modal overlay
        StackPane modalOverlay = new StackPane();
        modalOverlay.getChildren().addAll(mainContent, roadDetailsModal);
        StackPane.setAlignment(roadDetailsModal, Pos.CENTER);
        
        getChildren().addAll(header, modalOverlay);
        VBox.setVgrow(modalOverlay, Priority.ALWAYS);
        
        // Add AI assistant
        getChildren().add(aiAssistant);
    }
    
    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(15, 20, 15, 20));
        header.getStyleClass().add("header");
        
        // Header left - Logo
        HBox headerLeft = new HBox(12);
        headerLeft.setAlignment(Pos.CENTER_LEFT);
        
        Circle logoIcon = new Circle(20);
        logoIcon.setFill(Color.valueOf("#3498db"));
        
        Text logoText = new Text("Map");
        logoText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        logoText.setFill(Color.valueOf("#2c3e50"));
        
        headerLeft.getChildren().addAll(logoIcon, logoText);
        
        // Header center - Search
        HBox headerCenter = new HBox(10);
        headerCenter.setAlignment(Pos.CENTER);
        
        HBox searchContainer = new HBox(8);
        searchContainer.setAlignment(Pos.CENTER_LEFT);
        searchContainer.getStyleClass().add("search-container");
        
        Text searchIcon = new Text("🔍");
        searchIcon.setFont(Font.font(14));
        
        searchField.setPrefWidth(300);
        searchField.setPrefHeight(35);
        
        searchContainer.getChildren().addAll(searchIcon, searchField);
        headerCenter.getChildren().add(searchContainer);
        
        // Header right - User avatar
        HBox headerRight = new HBox();
        headerRight.setAlignment(Pos.CENTER_RIGHT);
        
        Circle userAvatar = new Circle(20);
        userAvatar.setFill(Color.valueOf("#95a5a6"));
        
        headerRight.getChildren().add(userAvatar);
        
        header.getChildren().addAll(headerLeft, headerCenter, headerRight);
        HBox.setHgrow(headerLeft, Priority.ALWAYS);
        HBox.setHgrow(headerCenter, Priority.ALWAYS);
        HBox.setHgrow(headerRight, Priority.ALWAYS);
        
        return header;
    }
    
    private VBox createSidebar() {
        VBox sidebar = new VBox(20);
        sidebar.setPrefWidth(300);
        sidebar.setPadding(new Insets(20));
        sidebar.getStyleClass().add("sidebar");

        // Back to Dashboard button
        Button backButton = new Button("← Back to Dashboard");
        backButton.getStyleClass().add("back-btn");
        backButton.setOnAction(e -> handleNavigation("Dashboard"));
        // Layer Legends Section
        VBox layerSection = createLayerSection("Layer Legends");
        
        // Crime Zones Layer
        VBox crimeZonesLayer = createLayerItem("⚠️", "Crime Zones", crimeZonesToggle);
        
        // Listen Layer
        VBox listenLayer = createLayerItem("📡", "Listen", listenToggle);
        VBox listenSubItems = createSubItems();
        listenLayer.getChildren().add(listenSubItems);
        
        // Traffic Management Section
        VBox trafficSection = createLayerSection("Traffic Management");
        
        // CCTV Cameras Layer
        VBox cctvLayer = createLayerItem("📹", "CCTV Cameras", cctvToggle);
        
        // Congested Roads Layer
        VBox congestedLayer = createLayerItem("🚗", "Congested Roads", congestedRoadsToggle);
        
        // CCTV Coverage Layer (no toggle)
        VBox cctvCoverageLayer = createLayerItem("📹", "CCTV Coverage", null);
        Text legalLabel = new Text("Legal");
        legalLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        legalLabel.setFill(Color.valueOf("#27ae60"));
        cctvCoverageLayer.getChildren().add(legalLabel);
        
        // Police Stations Layer (no toggle)
        VBox policeLayer = createLayerItem("👮", "Police Stations", null);
        
        // Add all sections to sidebar
        layerSection.getChildren().addAll(crimeZonesLayer, listenLayer);
        trafficSection.getChildren().addAll(cctvLayer, congestedLayer, cctvCoverageLayer, policeLayer);
        
        sidebar.getChildren().addAll(backButton, layerSection, trafficSection);
        return sidebar;
    }
    
    private VBox createLayerSection(String title) {
        VBox section = new VBox(15);
        
        Text titleText = new Text(title);
        titleText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        titleText.setFill(Color.valueOf("#2c3e50"));
        
        section.getChildren().add(titleText);
        return section;
    }
    
    private VBox createLayerItem(String icon, String label, ToggleButton toggle) {
        VBox layerItem = new VBox(8);
        
        HBox layerHeader = new HBox(12);
        layerHeader.setAlignment(Pos.CENTER_LEFT);
        
        Text iconText = new Text(icon);
        iconText.setFont(Font.font(16));
        
        Text labelText = new Text(label);
        labelText.setFont(Font.font("Segoe UI", 14));
        labelText.setFill(Color.valueOf("#2c3e50"));
        
        layerHeader.getChildren().add(iconText);
        layerHeader.getChildren().add(labelText);
        
        if (toggle != null) {
            HBox.setHgrow(labelText, Priority.ALWAYS);
            layerHeader.getChildren().add(toggle);
        }
        
        layerItem.getChildren().add(layerHeader);
        return layerItem;
    }
    
    private VBox createSubItems() {
        VBox subItems = new VBox(8);
        subItems.setPadding(new Insets(0, 0, 0, 28));
        
        HBox crimeSeverity = new HBox(8);
        crimeSeverity.setAlignment(Pos.CENTER_LEFT);
        
        Rectangle colorIndicator = new Rectangle(12, 12);
        colorIndicator.setFill(Color.valueOf("#e74c3c"));
        colorIndicator.setArcWidth(2);
        colorIndicator.setArcHeight(2);
        
        Text crimeText = new Text("Crime Severity @cellular");
        crimeText.setFont(Font.font("Segoe UI", 12));
        crimeText.setFill(Color.valueOf("#7f8c8d"));
        
        crimeSeverity.getChildren().addAll(colorIndicator, crimeText);
        
        Text legendsText = new Text("Legends");
        legendsText.setFont(Font.font("Segoe UI", 12));
        legendsText.setFill(Color.valueOf("#7f8c8d"));
        
        subItems.getChildren().addAll(crimeSeverity, legendsText);
        return subItems;
    }
    
    private void createMapElements() {
        // Add map elements to container
        mapContainer.getChildren().add(mapElements);
        
        // Create a simple map background
        Rectangle mapBackground = new Rectangle(800, 600);
        mapBackground.setFill(Color.valueOf("#e8f4f8"));
        mapBackground.setStroke(Color.valueOf("#bdc3c7"));
        mapBackground.setStrokeWidth(2);
        
        // Create roads
        createRoads();
        
        // Create buildings
        createBuildings();
        
        // Create traffic indicators
        createTrafficIndicators();
        
        // Create police stations
        createPoliceStations();
        
        // Create CCTV cameras
        createCCTVCameras();
        
        // Create crime zones
        createCrimeZones();
        
        mapElements.getChildren().add(0, mapBackground);
        
        // Create map legend
        createMapLegend();

        
        // Create road details modal
        createRoadDetailsModal();
    }
    
    private void createRoads() {
        // Main roads
        Rectangle mainRoad1 = new Rectangle(100, 200, 600, 20);
        mainRoad1.setFill(Color.valueOf("#34495e"));
        mainRoad1.setArcWidth(10);
        mainRoad1.setArcHeight(10);
        
        Rectangle mainRoad2 = new Rectangle(350, 50, 20, 500);
        mainRoad2.setFill(Color.valueOf("#34495e"));
        mainRoad2.setArcWidth(10);
        mainRoad2.setArcHeight(10);
        
        // Side roads
        Rectangle sideRoad1 = new Rectangle(200, 100, 300, 15);
        sideRoad1.setFill(Color.valueOf("#7f8c8d"));
        sideRoad1.setArcWidth(8);
        sideRoad1.setArcHeight(8);
        
        Rectangle sideRoad2 = new Rectangle(150, 350, 400, 15);
        sideRoad2.setFill(Color.valueOf("#7f8c8d"));
        sideRoad2.setArcWidth(8);
        sideRoad2.setArcHeight(8);
        
        // Add road labels
        Text roadLabel1 = new Text(110, 220, "York Place Road");
        roadLabel1.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        roadLabel1.setFill(Color.valueOf("#2c3e50"));
        
        Text roadLabel2 = new Text(370, 70, "Main Street");
        roadLabel2.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        roadLabel2.setFill(Color.valueOf("#2c3e50"));
        
        // Make roads clickable
        mainRoad1.setOnMouseClicked(e -> showRoadDetails("York Place Road", "Main Street"));
        mainRoad2.setOnMouseClicked(e -> showRoadDetails("Main Street", "Primary Route"));
        
        mapElements.getChildren().addAll(mainRoad1, mainRoad2, sideRoad1, sideRoad2, roadLabel1, roadLabel2);
    }
    
    private void createBuildings() {
        // Create some buildings
        Rectangle building1 = new Rectangle(120, 120, 60, 40);
        building1.setFill(Color.valueOf("#95a5a6"));
        building1.setArcWidth(5);
        building1.setArcHeight(5);
        
        Rectangle building2 = new Rectangle(400, 80, 80, 50);
        building2.setFill(Color.valueOf("#95a5a6"));
        building2.setArcWidth(5);
        building2.setArcHeight(5);
        
        Rectangle building3 = new Rectangle(200, 400, 70, 45);
        building3.setFill(Color.valueOf("#95a5a6"));
        building3.setArcWidth(5);
        building3.setArcHeight(5);
        
        mapElements.getChildren().addAll(building1, building2, building3);
    }
    
    private void createTrafficIndicators() {
        // Traffic lights
        Circle trafficLight1 = new Circle(380, 210, 8);
        trafficLight1.setFill(Color.valueOf("#e74c3c"));
        
        Circle trafficLight2 = new Circle(360, 210, 8);
        trafficLight2.setFill(Color.valueOf("#f39c12"));
        
        Circle trafficLight3 = new Circle(340, 210, 8);
        trafficLight3.setFill(Color.valueOf("#27ae60"));
        
        // Traffic flow indicators
        for (int i = 0; i < 5; i++) {
            Rectangle car = new Rectangle(150 + i * 30, 205, 15, 8);
            car.setFill(Color.valueOf("#3498db"));
            car.setArcWidth(2);
            car.setArcHeight(2);
            mapElements.getChildren().add(car);
        }
        
        mapElements.getChildren().addAll(trafficLight1, trafficLight2, trafficLight3);
    }
    
    private void createPoliceStations() {
        // Police station 1
        Rectangle policeStation1 = new Rectangle(500, 150, 40, 30);
        policeStation1.setFill(Color.valueOf("#e74c3c"));
        policeStation1.setArcWidth(5);
        policeStation1.setArcHeight(5);
        
        Text policeLabel1 = new Text(505, 170, "PS");
        policeLabel1.setFont(Font.font("Segoe UI", FontWeight.BOLD, 10));
        policeLabel1.setFill(Color.WHITE);
        
        // Police station 2
        Rectangle policeStation2 = new Rectangle(100, 450, 40, 30);
        policeStation2.setFill(Color.valueOf("#e74c3c"));
        policeStation2.setArcWidth(5);
        policeStation2.setArcHeight(5);
        
        Text policeLabel2 = new Text(105, 470, "PS");
        policeLabel2.setFont(Font.font("Segoe UI", FontWeight.BOLD, 10));
        policeLabel2.setFill(Color.WHITE);
        
        mapElements.getChildren().addAll(policeStation1, policeLabel1, policeStation2, policeLabel2);
    }
    
    private void createCCTVCameras() {
        // CCTV cameras
        for (int i = 0; i < 6; i++) {
            Circle camera = new Circle(150 + i * 100, 100 + (i % 2) * 300, 6);
            camera.setFill(Color.valueOf("#f39c12"));
            camera.setStroke(Color.valueOf("#e67e22"));
            camera.setStrokeWidth(2);
            
            // Add camera icon
            Text cameraIcon = new Text(150 + i * 100 - 3, 100 + (i % 2) * 300 + 3, "📹");
            cameraIcon.setFont(Font.font(8));
            
            mapElements.getChildren().addAll(camera, cameraIcon);
        }
    }
    
    private void createCrimeZones() {
        // Crime zone 1
        Polygon crimeZone1 = new Polygon();
        crimeZone1.getPoints().addAll(300.0, 250.0, 350.0, 200.0, 400.0, 250.0, 350.0, 300.0);
        crimeZone1.setFill(Color.valueOf("#e74c3c"));
        crimeZone1.setOpacity(0.3);
        crimeZone1.setStroke(Color.valueOf("#c0392b"));
        crimeZone1.setStrokeWidth(2);
        
        // Crime zone 2
        Circle crimeZone2 = new Circle(200, 350, 25);
        crimeZone2.setFill(Color.valueOf("#e74c3c"));
        crimeZone2.setOpacity(0.3);
        crimeZone2.setStroke(Color.valueOf("#c0392b"));
        crimeZone2.setStrokeWidth(2);
        
        mapElements.getChildren().addAll(crimeZone1, crimeZone2);
    }
    
    private void createMapLegend() {
        mapLegend.setPrefWidth(200);
        
        Text legendTitle = new Text("City Traffic Monitor");
        legendTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        legendTitle.setFill(Color.valueOf("#2c3e50"));
        
        Text legendSubtitle = new Text("Legal Compliance");
        legendSubtitle.setFont(Font.font("Segoe UI", 12));
        legendSubtitle.setFill(Color.valueOf("#7f8c8d"));
        
        // Color palette
        HBox colorPalette = new HBox(8);
        colorPalette.setAlignment(Pos.CENTER);
        
        String[] colors = {"#27ae60", "#e74c3c", "#3498db", "#2c3e50", "#95a5a6"};
        for (String color : colors) {
            Rectangle colorBox = new Rectangle(20, 20);
            colorBox.setFill(Color.valueOf(color));
            colorBox.setArcWidth(3);
            colorBox.setArcHeight(3);
            colorPalette.getChildren().add(colorBox);
        }
        
        Text termsText = new Text("Terms & Conditions");
        termsText.setFont(Font.font("Segoe UI", 10));
        termsText.setFill(Color.valueOf("#95a5a6"));
        
        mapLegend.getChildren().addAll(legendTitle, legendSubtitle, colorPalette, termsText);
    }

    
    private void createRoadDetailsModal() {
        roadDetailsModal.setPrefSize(400, 300);
        
        // Modal header
        HBox modalHeader = new HBox(15);
        modalHeader.setAlignment(Pos.CENTER_LEFT);
        
        Text modalTitle = new Text("Road Details");
        modalTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        modalTitle.setFill(Color.valueOf("#2c3e50"));
        
        Button closeButton = new Button("✕");
        closeButton.getStyleClass().add("close-btn");
        closeButton.setOnAction(e -> hideRoadDetails());
        
        modalHeader.getChildren().addAll(modalTitle, closeButton);
        HBox.setHgrow(modalTitle, Priority.ALWAYS);
        
        // Road info
        VBox roadInfo = new VBox(5);
        Text roadName = new Text("York Place Road");
        roadName.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        roadName.setFill(Color.valueOf("#2c3e50"));
        
        Text roadSubtitle = new Text("(Main Street)");
        roadSubtitle.setFont(Font.font("Segoe UI", 12));
        roadSubtitle.setFill(Color.valueOf("#7f8c8d"));
        
        roadInfo.getChildren().addAll(roadName, roadSubtitle);
        
        // Issue section
        HBox issueSection = new HBox(15);
        issueSection.setAlignment(Pos.CENTER_LEFT);
        
        Text issueText = new Text("Report Issue");
        issueText.setFont(Font.font("Segoe UI", 14));
        issueText.setFill(Color.valueOf("#2c3e50"));
        
        ToggleButton issueToggle = new ToggleButton();
        issueToggle.getStyleClass().add("toggle-switch");
        
        issueSection.getChildren().addAll(issueText, issueToggle);
        
        // Action buttons
        HBox actionButtons = new HBox(10);
        actionButtons.setAlignment(Pos.CENTER);
        
        Button detailsButton = new Button("Go to Details");
        detailsButton.getStyleClass().add("btn-secondary");
        
        Button reportButton = new Button("Report Issue");
        reportButton.getStyleClass().add("btn-primary");
        
        actionButtons.getChildren().addAll(detailsButton, reportButton);
        
        roadDetailsModal.getChildren().addAll(modalHeader, roadInfo, issueSection, actionButtons);
    }
    
    private void setupStyling() {
        getStyleClass().add("maps-view");
    }
    
    private void setupEventHandlers() {
        // Search functionality
        searchField.setOnAction(e -> handleSearch());
        
        // Layer toggle handlers
        crimeZonesToggle.setOnAction(e -> toggleCrimeZones());
        listenToggle.setOnAction(e -> toggleListen());
        cctvToggle.setOnAction(e -> toggleCCTV());
        congestedRoadsToggle.setOnAction(e -> toggleCongestedRoads());
    }
    
    private void handleSearch() {
        String searchTerm = searchField.getText();
        System.out.println("Searching for: " + searchTerm);
        // TODO: Implement map search functionality
    }
    
    private void toggleCrimeZones() {
        boolean visible = crimeZonesToggle.isSelected();
        System.out.println("Crime zones: " + (visible ? "ON" : "OFF"));
        // TODO: Toggle crime zones visibility on map
    }
    
    private void toggleListen() {
        boolean visible = listenToggle.isSelected();
        System.out.println("Listen layer: " + (visible ? "ON" : "OFF"));
        // TODO: Toggle listen layer visibility
    }
    
    private void toggleCCTV() {
        boolean visible = cctvToggle.isSelected();
        System.out.println("CCTV cameras: " + (visible ? "ON" : "OFF"));
        // TODO: Toggle CCTV cameras visibility
    }
    
    private void toggleCongestedRoads() {
        boolean visible = congestedRoadsToggle.isSelected();
        System.out.println("Congested roads: " + (visible ? "ON" : "OFF"));
        // TODO: Toggle congested roads visibility
    }
    
    private void showRoadDetails(String roadName, String roadType) {
        System.out.println("Showing details for: " + roadName + " (" + roadType + ")");
        roadDetailsModal.setVisible(true);
    }
    
    private void hideRoadDetails() {
        roadDetailsModal.setVisible(false);
    }

    private void handleNavigation(String destination) {
        System.out.println("Navigating to: " + destination);
        // TODO: Implement navigation to other views
        mainController.showDashboardView();
    }
} 