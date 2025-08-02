package com.roadster.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ToolBar;
import javafx.scene.Parent;
import com.roadster.views.*;
import java.io.IOException;

/**
 * Main Controller - Handles navigation between different views
 * This is the central controller for the application
 */
public class MainController {
    
    @FXML
    private BorderPane mainContainer;

    
    // View instances
    private LoginView loginView;
    private DashboardView dashboardView;
    private DriversView driversView;
    private Parent mapsView; // Changed to Parent for FXML-loaded view
    private MapsController mapsController; // Reference to the maps controller
    private PoliceBoxView policeBoxView;
    private UserProfileView userProfileView;
    
    @FXML
    public void initialize() {
        // Initialize all views
        loginView = new LoginView(this);
        dashboardView = new DashboardView(this);
        driversView = new DriversView(this);
        policeBoxView = new PoliceBoxView(this);
        userProfileView = new UserProfileView();
        
        // Initialize FXML-based maps view
        initializeMapsView();
        
        // Set default view (login)
        showLoginView();
    }
    
    private void initializeMapsView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MapsView.fxml"));
            mapsView = loader.load();
            mapsController = loader.getController();
            mapsController.setMainController(this);
        } catch (IOException e) {
            System.err.println("Error loading MapsView.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    
    @FXML
    private void showLoginView() {
        mainContainer.setCenter(loginView);
    }
    
    @FXML
    public void showDashboardView() {
        mainContainer.setCenter(dashboardView);
    }
    
    @FXML
    public void showDriversView() {
        mainContainer.setCenter(driversView);
    }
    
    @FXML
    public void showMapsView() {
        if (mapsView != null) {
            mainContainer.setCenter(mapsView);
        } else {
            System.err.println("MapsView is not properly initialized");
        }
    }
    
    @FXML
    public void showPoliceBoxView() {
        mainContainer.setCenter(policeBoxView);
    }
    
    @FXML
    public void showUserProfileView() {
        mainContainer.setCenter(userProfileView);
    }
}
