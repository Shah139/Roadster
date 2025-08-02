package com.roadster.controllers;

import com.roadster.controllers.MainController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.concurrent.Worker;

import java.net.URL;
import java.util.ResourceBundle;

public class MapsController implements Initializable {
    @FXML
    private WebView mapWebView;

    @FXML
    private Button backButton;

    private WebEngine webEngine;
    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        webEngine = mapWebView.getEngine();

        // Load the map HTML file
        URL mapUrl = getClass().getResource("/html/map.html");
        if (mapUrl != null) {
            webEngine.load(mapUrl.toString());
            System.out.println("Loading map from: " + mapUrl.toString());
        } else {
            System.err.println("Could not find map.html file");
        }

        // Enable JavaScript
        webEngine.setJavaScriptEnabled(true);

        // Wait for page to load before enabling interactions
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                System.out.println("Map loaded successfully!");
            } else if (newState == Worker.State.FAILED) {
                System.err.println("Failed to load map");
            }
        });
    }

    @FXML
    protected void onBackToDashboard() {
        if (mainController != null) {
            mainController.showDashboardView();
        }
    }

    // Method to add a marker (for future use when receiving location data)
    public void addMarker(double lat, double lng, String popupText) {
        if (webEngine != null) {
            String script = String.format("window.mapFunctions.addMarker(%f, %f, '%s');", lat, lng, popupText);
            webEngine.executeScript(script);
        }
    }

    // Method to center map on specific coordinates (for future use)
    public void centerMap(double lat, double lng) {
        if (webEngine != null) {
            String script = String.format("window.mapFunctions.centerMap(%f, %f, 15);", lat, lng);
            webEngine.executeScript(script);
        }
    }
}
