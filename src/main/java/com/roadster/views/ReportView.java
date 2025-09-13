package com.roadster.views;

import com.roadster.components.SideBar;
import com.roadster.controllers.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ReportView extends HBox {

    private VBox sidebar;
    private VBox mainContent;
    private MainController mainController;
    private String selectedCity;
    
    // Form components
    private ComboBox<String> incidentTypeCombo;
    private TextField locationField;
    private TextArea descriptionArea;
    private ComboBox<String> urgencyCombo;
    private CheckBox anonymousCheck;
    private Button submitButton;

    public ReportView(MainController mainController) {
        this.mainController = mainController;
        this.selectedCity = DashboardView.cityDropdown != null ? DashboardView.cityDropdown.getValue() : "Dhaka";
        initializeComponents();
        setupLayout();
        setupStyling();
        setupEventHandlers();
    }
    
    public ReportView(MainController mainController, String city) {
        this.mainController = mainController;
        this.selectedCity = city != null ? city : "Dhaka";
        initializeComponents();
        setupLayout();
        setupStyling();
        setupEventHandlers();
    }

    private void initializeComponents() {
        // Sidebar
        SideBar sideBar = new SideBar(mainController);
        sidebar = sideBar.createSidebar();
        
        // Main content
        mainContent = new VBox(30);
        mainContent.setPadding(new Insets(30));
        mainContent.setAlignment(Pos.TOP_CENTER);
        
        // Initialize form components
        incidentTypeCombo = new ComboBox<>();
        locationField = new TextField();
        descriptionArea = new TextArea();
        urgencyCombo = new ComboBox<>();
        anonymousCheck = new CheckBox("Report anonymously");
        submitButton = new Button("Submit Report");
        
        setupFormComponents();
    }
    
    private void setupFormComponents() {
        // Incident type options
        ObservableList<String> incidentTypes = FXCollections.observableArrayList(
            "Traffic Accident",
            "Road Blockage",
            "Traffic Violation",
            "Road Damage/Pothole",
            "Illegal Parking",
            "Traffic Light Malfunction",
            "Emergency Vehicle Access Issue",
            "Suspicious Activity",
            "Infrastructure Issue",
            "Other"
        );
        incidentTypeCombo.setItems(incidentTypes);
        incidentTypeCombo.setPromptText("Select incident type");
        incidentTypeCombo.setPrefWidth(300);
        
        // Urgency levels
        ObservableList<String> urgencyLevels = FXCollections.observableArrayList(
            "Low - Minor issue, can wait",
            "Medium - Should be addressed soon",
            "High - Requires immediate attention",
            "Emergency - Life threatening situation"
        );
        urgencyCombo.setItems(urgencyLevels);
        urgencyCombo.setPromptText("Select urgency level");
        urgencyCombo.setPrefWidth(300);
        
        // Location field
        locationField.setPromptText("Enter specific location or landmark");
        locationField.setPrefWidth(300);
        
        // Description area
        descriptionArea.setPromptText("Provide detailed description of the incident...");
        descriptionArea.setPrefRowCount(5);
        descriptionArea.setPrefWidth(300);
        descriptionArea.setWrapText(true);
        
        // Submit button styling
        submitButton.setPrefWidth(200);
        submitButton.setPrefHeight(40);
        submitButton.setStyle(
            "-fx-background-color: #27ae60;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
        );
    }

    private void setupLayout() {
        // Header section
        VBox header = createHeaderSection();
        
        // Form section
        VBox formSection = createFormSection();
        
        // Add sections to main content
        mainContent.getChildren().addAll(header, formSection);
        
        // Add sidebar and main content to HBox
        this.getChildren().addAll(sidebar, mainContent);
        
        // Set grow priorities
        HBox.setHgrow(mainContent, Priority.ALWAYS);
    }
    
    private VBox createHeaderSection() {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(0, 0, 20, 0));
        
        Text title = new Text("Report an Incident");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        title.setFill(Color.valueOf("#2c3e50"));
        
        Text subtitle = new Text("Report incidents in: " + selectedCity);
        subtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 16));
        subtitle.setFill(Color.valueOf("#7f8c8d"));
        
        Text instruction = new Text("Help improve road safety by reporting incidents you observe");
        instruction.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        instruction.setFill(Color.valueOf("#95a5a6"));
        
        header.getChildren().addAll(title, subtitle, instruction);
        return header;
    }
    
    private VBox createFormSection() {
        VBox formSection = new VBox(20);
        formSection.setAlignment(Pos.CENTER);
        formSection.setPadding(new Insets(20));
        formSection.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);"
        );
        formSection.setPrefWidth(400);
        
        // Form fields with labels
        VBox typeSection = createFormField("Incident Type *", incidentTypeCombo);
        VBox locationSection = createFormField("Location *", locationField);
        VBox urgencySection = createFormField("Urgency Level *", urgencyCombo);
        VBox descriptionSection = createFormField("Description *", descriptionArea);
        
        // Anonymous checkbox section
        VBox checkSection = new VBox(5);
        checkSection.setAlignment(Pos.CENTER_LEFT);
        anonymousCheck.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 14px;");
        checkSection.getChildren().add(anonymousCheck);
        
        // Button section
        VBox buttonSection = new VBox(10);
        buttonSection.setAlignment(Pos.CENTER);
        
        Text note = new Text("* Required fields");
        note.setFont(Font.font("Segoe UI", 12));
        note.setFill(Color.valueOf("#95a5a6"));
        
        buttonSection.getChildren().addAll(note, submitButton);
        
        formSection.getChildren().addAll(
            typeSection, locationSection, urgencySection, 
            descriptionSection, checkSection, buttonSection
        );
        
        return formSection;
    }
    
    private VBox createFormField(String labelText, Control control) {
        VBox fieldSection = new VBox(8);
        fieldSection.setAlignment(Pos.CENTER_LEFT);
        
        Text label = new Text(labelText);
        label.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        label.setFill(Color.valueOf("#2c3e50"));
        
        // Style the control
        control.setStyle(
            "-fx-background-color: #f8f9fa;" +
            "-fx-border-color: #dee2e6;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 10;" +
            "-fx-font-size: 14px;"
        );
        
        fieldSection.getChildren().addAll(label, control);
        return fieldSection;
    }

    private void setupStyling() {
        // Main styling
        this.setStyle("-fx-background-color: #f5f6fa;");
        mainContent.setStyle("-fx-background-color: transparent;");
    }

    private void setupEventHandlers() {
        // Submit button action
        submitButton.setOnAction(e -> handleSubmitReport());
        
        // Hover effects for submit button
        submitButton.setOnMouseEntered(e -> 
            submitButton.setStyle(
                "-fx-background-color: #229954;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;"
            )
        );
        
        submitButton.setOnMouseExited(e -> 
            submitButton.setStyle(
                "-fx-background-color: #27ae60;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;"
            )
        );
    }
    
    private void handleSubmitReport() {
        // Validate required fields
        if (!validateForm()) {
            return;
        }
        
        // Get form data
        String incidentType = incidentTypeCombo.getValue();
        String location = locationField.getText().trim();
        
        // For now, just show a success message
        // In a real app, you would send this to your API with all form data
        showSuccessDialog(incidentType, location);
        
        // Clear form
        clearForm();
    }
    
    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();
        
        if (incidentTypeCombo.getValue() == null) {
            errors.append("• Please select an incident type\n");
        }
        
        if (locationField.getText().trim().isEmpty()) {
            errors.append("• Please enter a location\n");
        }
        
        if (urgencyCombo.getValue() == null) {
            errors.append("• Please select urgency level\n");
        }
        
        if (descriptionArea.getText().trim().isEmpty()) {
            errors.append("• Please provide a description\n");
        }
        
        if (errors.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete Form");
            alert.setHeaderText("Please complete all required fields:");
            alert.setContentText(errors.toString());
            alert.showAndWait();
            return false;
        }
        
        return true;
    }
    
    private void showSuccessDialog(String incidentType, String location) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Report Submitted");
        alert.setHeaderText("Thank you for your report!");
        alert.setContentText(String.format(
            "Your %s report for %s in %s has been submitted successfully.\n\n" +
            "Report ID: #RPT-%d\n" +
            "Our team will review it shortly.",
            incidentType, location, selectedCity, System.currentTimeMillis() % 100000
        ));
        alert.showAndWait();
    }
    
    private void clearForm() {
        incidentTypeCombo.setValue(null);
        locationField.clear();
        urgencyCombo.setValue(null);
        descriptionArea.clear();
        anonymousCheck.setSelected(false);
    }
}