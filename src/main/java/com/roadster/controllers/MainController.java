package com.roadster.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ToolBar;
import com.roadster.views.*;

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
    private MapsView mapsView;
    private PoliceBoxView policeBoxView;
    private UserProfileView userProfileView;
    
    @FXML
    public void initialize() {
        // Initialize all views
        loginView = new LoginView(this);
        dashboardView = new DashboardView(this);
        driversView = new DriversView(this);
        mapsView = new MapsView(this);
        policeBoxView = new PoliceBoxView(this);
        userProfileView = new UserProfileView();
        
        // Set default view (login)
        showLoginView();

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
        mainContainer.setCenter(mapsView);
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