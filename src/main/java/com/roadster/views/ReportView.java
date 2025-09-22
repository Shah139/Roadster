package com.roadster.views;

import com.roadster.components.SideBar;
import com.roadster.controllers.MainController;
import com.roadster.service.ApiService;
import com.roadster.models.Report;
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
import javafx.concurrent.Task;
import javafx.application.Platform;
import java.io.StringWriter;
import java.io.PrintWriter;

public class ReportView extends HBox {

    private VBox sidebar;
    private VBox mainContent;
    private MainController mainController;
    private String selectedCity;
    
    // Form components
    private ComboBox<String> incidentTypeCombo;  // Maps to reportType
    private TextField locationField;             // Maps to locationName (query param)
    private TextArea descriptionArea;            // Maps to description
    private ComboBox<String> statusCombo;        // Maps to status
    private TextField districtField;             // Maps to district (query param)
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
        statusCombo = new ComboBox<>();
        districtField = new TextField();
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
        

        
        // Location field
        locationField.setPromptText("Enter specific location or landmark");
        locationField.setPrefWidth(300);
        
        // Description area
        descriptionArea.setPromptText("Provide detailed description of the incident...");
        descriptionArea.setPrefRowCount(5);
        descriptionArea.setPrefWidth(300);
        descriptionArea.setWrapText(true);
        
        // Status combo
        ObservableList<String> statusOptions = FXCollections.observableArrayList(
            "PENDING",
            "IN_PROGRESS", 
            "RESOLVED",
            "REJECTED"
        );
        statusCombo.setItems(statusOptions);
        statusCombo.setPromptText("Select status");
        statusCombo.setPrefWidth(300);
        statusCombo.setValue("PENDING"); // Default to PENDING
        
        // District field
        districtField.setPromptText("Enter district name");
        districtField.setPrefWidth(300);
        districtField.setText(selectedCity); // Pre-fill with selected city
        
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
        VBox locationSection = createFormField("Location Name *", locationField);
        VBox districtSection = createFormField("District *", districtField);
        VBox descriptionSection = createFormField("Description *", descriptionArea);
        VBox statusSection = createFormField("Status *", statusCombo);
        
        // Button section
        VBox buttonSection = new VBox(10);
        buttonSection.setAlignment(Pos.CENTER);
        
        Text note = new Text("* Required fields");
        note.setFont(Font.font("Segoe UI", 12));
        note.setFill(Color.valueOf("#95a5a6"));
        
        buttonSection.getChildren().addAll(note, submitButton);
        
        formSection.getChildren().addAll(
            typeSection, locationSection, districtSection, descriptionSection, statusSection, buttonSection
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

        // Get form data (all fields for backend)
        String reportType = incidentTypeCombo.getValue();          // Request body: reportType
        String locationName = locationField.getText().trim();      // Query parameter: locationName
        String district = districtField.getText().trim();          // Query parameter: district
        String description = descriptionArea.getText().trim();     // Request body: description
        String status = statusCombo.getValue();                    // Request body: status
        // timestamp will be auto-generated in backend

        // Show progress indicator
        Alert progressAlert = new Alert(Alert.AlertType.INFORMATION);
        progressAlert.setTitle("Submitting Report");
        progressAlert.setHeaderText("Please wait...");
        progressAlert.setContentText("Submitting your report to the database.");
        progressAlert.show();

        // Debug: Print out all relevant info before submission
        System.out.println("[DEBUG] Submitting report with:");
        System.out.println("  ReportType: " + reportType);
        System.out.println("  LocationName: " + locationName);
        System.out.println("  District: " + district);
        System.out.println("  Description: " + description);
        System.out.println("  Status: " + status);

        // Submit in background thread to avoid blocking UI
        Task<Report> submitTask = new Task<Report>() {
            @Override
            protected Report call() throws Exception {
                // Debug: Try/catch to print more info if error
                try {
                    Report result = ApiService.submitReport(reportType, description, status, locationName, district);
                    System.out.println("[DEBUG] Report submission successful.");
                    return result;
                } catch (Exception ex) {
                    System.out.println("[DEBUG] Report submission failed: " + ex.getMessage());
                    ex.printStackTrace();
                    throw ex;
                }
            }
        };

        submitTask.setOnSucceeded(e -> {
            Platform.runLater(() -> {
                progressAlert.close();
                Report submittedReport = submitTask.getValue();
                showSuccessDialog(submittedReport);
                clearForm();
            });
        });

        submitTask.setOnFailed(e -> {
            Platform.runLater(() -> {
                progressAlert.close();
                Throwable exception = submitTask.getException();
                // Debug: Print stack trace in UI error dialog
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                exception.printStackTrace(pw);
                String stackTrace = sw.toString();
                showErrorDialog(exception.getMessage() + "\n\nStacktrace:\n" + stackTrace);
            });
        });

        // Start the task in a new thread
        Thread submitThread = new Thread(submitTask);
        submitThread.setDaemon(true);
        submitThread.start();
    }
    
    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();
        
        if (incidentTypeCombo.getValue() == null) {
            errors.append("• Please select an incident type\n");
        }
        
        if (locationField.getText().trim().isEmpty()) {
            errors.append("• Please enter a location name\n");
        }
        
        if (districtField.getText().trim().isEmpty()) {
            errors.append("• Please enter a district\n");
        }
        
        if (descriptionArea.getText().trim().isEmpty()) {
            errors.append("• Please provide a description\n");
        }
        
        if (statusCombo.getValue() == null) {
            errors.append("• Please select a status\n");
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
    
    private void showSuccessDialog(Report submittedReport) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Report Submitted Successfully");
        alert.setHeaderText("Thank you for your report!");
        alert.setContentText(String.format(
            "Your report has been submitted to the database successfully.\n\n" +
            "Report ID: #%d\n" +
            "Report Type: %s\n" +
            "Status: %s\n" +
            "Timestamp: %s\n\n" +
            "Our team will review it shortly.",
            submittedReport.getReportId(),
            submittedReport.getReportType(),
            submittedReport.getStatus(),
            submittedReport.getTimestamp().toString()
        ));
        alert.showAndWait();
    }
    
    private void showErrorDialog(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Submission Failed");
        alert.setHeaderText("Failed to submit your report");
        alert.setContentText(String.format(
            "There was an error submitting your report:\n\n%s\n\n" +
            "Please check your internet connection and try again.",
            errorMessage
        ));
        // Also print to console for debugging
        System.out.println("[DEBUG] Error dialog shown: " + errorMessage);
        alert.showAndWait();
    }
    
    private void clearForm() {
        incidentTypeCombo.setValue(null);
        locationField.clear();
        districtField.setText(selectedCity); // Reset to selected city
        descriptionArea.clear();
        statusCombo.setValue("PENDING"); // Reset to default status
    }
}