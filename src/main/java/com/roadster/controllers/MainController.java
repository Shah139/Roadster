package com.roadster.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import com.roadster.views.*;

/**
 * Main Controller - Handles navigation between different views
 * This is the central controller for the application
 */
public class MainController {
    
    @FXML
    private BorderPane mainContainer;
    
    @FXML
    private ToolBar navigationBar;
    
    // View instances
    private LoginView loginView;
    private DashboardView dashboardView;
    private DriversView driversView;
    private MapsView mapsView;
    private PoliceBoxView policeBoxView;
    private UserProfileView userProfileView;
    
    @FXML
    public void initialize() {
        // Initialize all views
        loginView = new LoginView();
        dashboardView = new DashboardView();
        driversView = new DriversView();
        mapsView = new MapsView();
        policeBoxView = new PoliceBoxView();
        userProfileView = new UserProfileView();
        
        // Set default view (login)
        showLoginView();
        
        // Setup navigation buttons
        setupNavigation();
    }
    
    private void setupNavigation() {
        // TODO: Add navigation buttons to toolbar
        // TODO: Handle navigation between views
    }
    
    @FXML
    private void showLoginView() {
        mainContainer.setCenter(loginView);
        navigationBar.setVisible(false);
        navigationBar.getItems().clear();
    }
    
    @FXML
    private void showDashboardView() {
        mainContainer.setCenter(dashboardView);
        navigationBar.setVisible(true);
    }
    
    @FXML
    private void showDriversView() {
        mainContainer.setCenter(driversView);
        navigationBar.setVisible(true);
    }
    
    @FXML
    private void showMapsView() {
        mainContainer.setCenter(mapsView);
        navigationBar.setVisible(true);
    }
    
    @FXML
    private void showPoliceBoxView() {
        mainContainer.setCenter(policeBoxView);
        navigationBar.setVisible(true);
    }
    
    @FXML
    private void showUserProfileView() {
        mainContainer.setCenter(userProfileView);
        navigationBar.setVisible(true);
    }
} 