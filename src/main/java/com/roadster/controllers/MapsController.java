package com.roadster.controllers;

import com.roadster.controllers.MainController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.concurrent.Worker;
import netscape.javascript.JSObject;

import java.net.URL;
import java.util.ResourceBundle;

public class MapsController implements Initializable {
    @FXML
    private WebView mapWebView;

    @FXML
    private Button backButton;
    
    @FXML
    private Label statusLabel;

    private WebEngine webEngine;
    private MainController mainController;
    private boolean isMapReady = false;

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
            System.out.println("Loading real-time phone tracking map from: " + mapUrl.toString());
        } else {
            System.err.println("Could not find map.html file");
        }

        // Enable JavaScript
        webEngine.setJavaScriptEnabled(true);

        // Wait for page to load before enabling interactions
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                System.out.println("Phone tracking map loaded successfully!");
                isMapReady = true;
                
                // Set up JavaScript bridge for communication between Java and HTML
                setupJavaScriptBridge();
                
                // Auto-connect to WebSocket after map loads
                Platform.runLater(() -> {
                    if (statusLabel != null) {
                        statusLabel.setText("Map loaded. Ready for phone tracking.");
                    }
                    // Automatically connect to WebSocket
                    connectToWebSocket();
                });
                
            } else if (newState == Worker.State.FAILED) {
                System.err.println("Failed to load phone tracking map");
                Platform.runLater(() -> {
                    if (statusLabel != null) {
                        statusLabel.setText("Failed to load map");
                    }
                });
            }
        });
    }
    
    private void setupJavaScriptBridge() {
        try {
            // Create a bridge object for JavaScript to call Java methods
            JSObject window = (JSObject) webEngine.executeScript("window");
            window.setMember("java", new JavaScriptBridge());
            
            System.out.println("JavaScript bridge established");
        } catch (Exception e) {
            System.err.println("Failed to setup JavaScript bridge: " + e.getMessage());
        }
    }

    @FXML
    protected void onBackToDashboard() {
        // Disconnect WebSocket before leaving
        disconnectWebSocket();
        
        if (mainController != null) {
            mainController.showDashboardView();
        }
    }
    
    // Auto-connect to WebSocket
    public void connectToWebSocket() {
        if (isMapReady && webEngine != null) {
            try {
                webEngine.executeScript("if (window.mapFunctions && window.mapFunctions.connectWebSocket) { window.mapFunctions.connectWebSocket(); }");
                System.out.println("Attempting to connect to phone tracking WebSocket...");
            } catch (Exception e) {
                System.err.println("Failed to connect to WebSocket: " + e.getMessage());
            }
        }
    }
    
    // Disconnect from WebSocket
    public void disconnectWebSocket() {
        if (isMapReady && webEngine != null) {
            try {
                webEngine.executeScript("if (window.mapFunctions && window.mapFunctions.disconnectWebSocket) { window.mapFunctions.disconnectWebSocket(); }");
                System.out.println("Disconnecting from phone tracking WebSocket...");
            } catch (Exception e) {
                System.err.println("Failed to disconnect from WebSocket: " + e.getMessage());
            }
        }
    }
    
    // Center map on phone location
    public void centerOnPhone() {
        if (isMapReady && webEngine != null) {
            try {
                webEngine.executeScript("if (window.mapFunctions && window.mapFunctions.centerOnPhone) { window.mapFunctions.centerOnPhone(); }");
            } catch (Exception e) {
                System.err.println("Failed to center on phone: " + e.getMessage());
            }
        }
    }
    
    // Clear location trail
    public void clearTrail() {
        if (isMapReady && webEngine != null) {
            try {
                webEngine.executeScript("if (window.mapFunctions && window.mapFunctions.clearTrail) { window.mapFunctions.clearTrail(); }");
            } catch (Exception e) {
                System.err.println("Failed to clear trail: " + e.getMessage());
            }
        }
    }

    // Method to add a marker (for future use when receiving location data)
    public void addMarker(double lat, double lng, String popupText) {
        if (isMapReady && webEngine != null) {
            String script = String.format("if (window.mapFunctions && window.mapFunctions.addMarker) { window.mapFunctions.addMarker(%f, %f, '%s'); }", lat, lng, popupText);
            webEngine.executeScript(script);
        }
    }

    // Method to center map on specific coordinates
    public void centerMap(double lat, double lng) {
        if (isMapReady && webEngine != null) {
            String script = String.format("if (window.mapFunctions && window.mapFunctions.centerMap) { window.mapFunctions.centerMap(%f, %f, 15); }", lat, lng);
            webEngine.executeScript(script);
        }
    }
    
    // Inner class for JavaScript bridge
    public class JavaScriptBridge {
        
        public void onWebSocketConnected() {
            Platform.runLater(() -> {
                System.out.println("✅ Phone tracking WebSocket connected successfully!");
                if (statusLabel != null) {
                    statusLabel.setText("🟢 Connected - Waiting for phone location...");
                }
            });
        }
        
        public void onLocationUpdate(double lat, double lng) {
            Platform.runLater(() -> {
                System.out.println(String.format("📱 Phone location updated: %.6f, %.6f", lat, lng));
                if (statusLabel != null) {
                    statusLabel.setText(String.format("📱 Phone: %.4f, %.4f", lat, lng));
                }
                
                // You can add additional Java-side processing here
                // For example, logging to database, notifications, etc.
            });
        }
        
        public void onWebSocketDisconnected() {
            Platform.runLater(() -> {
                System.out.println("🔴 Phone tracking WebSocket disconnected");
                if (statusLabel != null) {
                    statusLabel.setText("🔴 Disconnected from tracking server");
                }
            });
        }
        
        public void onWebSocketError(String error) {
            Platform.runLater(() -> {
                System.err.println("❌ WebSocket Error: " + error);
                if (statusLabel != null) {
                    statusLabel.setText("❌ Connection Error");
                }
            });
        }
        
        public void log(String message) {
            System.out.println("JS Log: " + message);
        }
    }
}
