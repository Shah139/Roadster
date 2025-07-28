package com.roadster.views;

import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.BlurType;

/**
 * Login View - JavaFX equivalent of login-page
 * This will contain the login form and authentication logic
 */
public class LoginView extends HBox {
    
    private VBox loginForm;
    private VBox signupForm;
    private TextField emailField;
    private PasswordField passwordField;
    private TextField fullNameField;
    private TextField signupEmailField;
    private PasswordField signupPasswordField;
    private PasswordField confirmPasswordField;
    private ComboBox<String> roleComboBox;
    private CheckBox rememberMeCheckBox;
    private CheckBox agreeTermsCheckBox;
    private Button loginButton;
    private Button signupButton;
    private Button toggleFormButton;
    
    public LoginView() {
        initializeComponents();
        setupLayout();
        setupStyling();
        setupEventHandlers();
    }
    
    private void initializeComponents() {
        // Login form components
        emailField = new TextField();
        emailField.setPromptText("Enter your email");
        
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        
        rememberMeCheckBox = new CheckBox("Remember me");
        
        loginButton = new Button("Sign In");
        loginButton.getStyleClass().add("login-btn");
        
        // Signup form components
        fullNameField = new TextField();
        fullNameField.setPromptText("Enter your full name");
        
        signupEmailField = new TextField();
        signupEmailField.setPromptText("Enter your email");
        
        signupPasswordField = new PasswordField();
        signupPasswordField.setPromptText("Create a password");
        
        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm your password");
        
        roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Select your role", "User", "Driver");
        roleComboBox.setValue("Select your role");
        
        agreeTermsCheckBox = new CheckBox("I agree to the Terms of Service and Privacy Policy");
        
        signupButton = new Button("Create Account");
        signupButton.getStyleClass().add("login-btn");
        
        // Toggle button
        toggleFormButton = new Button("Don't have an account? Sign up here");
        toggleFormButton.getStyleClass().add("toggle-link");
    }
    
    private void setupLayout() {
        setSpacing(0);
        setAlignment(Pos.CENTER);
        
        // Left side - Branding
        VBox leftSide = createBrandingSection();
        
        // Right side - Forms
        VBox rightSide = createFormsSection();
        
        getChildren().addAll(leftSide, rightSide);
        HBox.setHgrow(leftSide, Priority.ALWAYS);
        HBox.setHgrow(rightSide, Priority.ALWAYS);
    }
    
    private VBox createBrandingSection() {
        VBox brandingSection = new VBox(40);
        brandingSection.setAlignment(Pos.CENTER);
        brandingSection.setPadding(new Insets(40));
        brandingSection.getStyleClass().add("login-left");
        
        // Logo
        HBox logo = new HBox(16);
        logo.setAlignment(Pos.CENTER);
        
        Circle logoIcon = new Circle(24);
        logoIcon.setFill(Color.WHITE);
        logoIcon.setOpacity(0.2);
        
        Text logoText = new Text("Roadster");
        logoText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        logoText.setFill(Color.WHITE);
        
        logo.getChildren().addAll(logoIcon, logoText);
        
        // Welcome text
        Text welcomeText = new Text("Welcome Back");
        welcomeText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 36));
        welcomeText.setFill(Color.WHITE);
        
        Text subtitleText = new Text("Sign in to access your traffic management dashboard");
        subtitleText.setFont(Font.font("Segoe UI", 18));
        subtitleText.setFill(Color.WHITE);
        subtitleText.setOpacity(0.9);
        
        // Features list
        VBox featuresList = new VBox(20);
        featuresList.setAlignment(Pos.CENTER_LEFT);
        
        String[] features = {
            "Real-time Analytics",
            "Interactive Maps", 
            "Driver Management",
            "Police Box Monitoring"
        };
        
        String[] icons = {"📊", "🗺️", "🚗", "👮"};
        
        for (int i = 0; i < features.length; i++) {
            HBox featureItem = new HBox(12);
            featureItem.setAlignment(Pos.CENTER_LEFT);
            
            Text icon = new Text(icons[i]);
            icon.setFont(Font.font(20));
            icon.setFill(Color.WHITE);
            
            Text featureText = new Text(features[i]);
            featureText.setFont(Font.font("Segoe UI", 16));
            featureText.setFill(Color.WHITE);
            
            featureItem.getChildren().addAll(icon, featureText);
            featuresList.getChildren().add(featureItem);
        }
        
        brandingSection.getChildren().addAll(logo, welcomeText, subtitleText, featuresList);
        return brandingSection;
    }
    
    private VBox createFormsSection() {
        VBox formsSection = new VBox();
        formsSection.setAlignment(Pos.CENTER);
        formsSection.setPadding(new Insets(40));
        formsSection.getStyleClass().add("login-right");
        
        // Create a scroll pane for the forms
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        
        // Container for forms
        VBox formsContainer = new VBox();
        formsContainer.setAlignment(Pos.CENTER);
        formsContainer.setMaxWidth(400);
        
        // Login form
        loginForm = createLoginForm();
        
        // Signup form (initially hidden)
        signupForm = createSignupForm();
        signupForm.setVisible(false);
        
        formsContainer.getChildren().addAll(loginForm, signupForm);
        scrollPane.setContent(formsContainer);
        
        formsSection.getChildren().add(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        
        return formsSection;
    }
    
    private VBox createLoginForm() {
        VBox form = new VBox(25);
        form.setAlignment(Pos.CENTER);
        form.setMaxWidth(400);
        form.getStyleClass().add("login-form");
        
        // Header
        VBox header = new VBox(8);
        header.setAlignment(Pos.CENTER);
        
        Text title = new Text("Sign In");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        title.setFill(Color.valueOf("#2c3e50"));
        
        Text subtitle = new Text("Enter your credentials to access your account");
        subtitle.setFont(Font.font("Segoe UI", 14));
        subtitle.setFill(Color.valueOf("#7f8c8d"));
        
        header.getChildren().addAll(title, subtitle);
        
        // Form fields
        VBox formFields = new VBox(15);
        
        // Email field
        VBox emailGroup = createFormGroup("Email Address", emailField, "📧");
        
        // Password field
        VBox passwordGroup = createFormGroup("Password", passwordField, "🔒");
        
        // Remember me and forgot password
        HBox optionsRow = new HBox();
        optionsRow.setAlignment(Pos.CENTER_LEFT);
        optionsRow.setSpacing(20);
        
        Text forgotPassword = new Text("Forgot Password?");
        forgotPassword.setFont(Font.font("Segoe UI", 12));
        forgotPassword.setFill(Color.valueOf("#3498db"));
        forgotPassword.setStyle("-fx-cursor: hand;");
        
        optionsRow.getChildren().addAll(rememberMeCheckBox, forgotPassword);
        
        formFields.getChildren().addAll(emailGroup, passwordGroup, optionsRow);
        
        // Login button
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setPrefHeight(45);
        
        // Toggle to signup
        toggleFormButton.setMaxWidth(Double.MAX_VALUE);
        toggleFormButton.getStyleClass().add("toggle-link");
        
        form.getChildren().addAll(header, formFields, loginButton, toggleFormButton);
        return form;
    }
    
    private VBox createSignupForm() {
        VBox form = new VBox(25);
        form.setAlignment(Pos.CENTER);
        form.setMaxWidth(400);
        form.getStyleClass().add("signup-form");
        
        // Header
        VBox header = new VBox(8);
        header.setAlignment(Pos.CENTER);
        
        Text title = new Text("Create Account");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        title.setFill(Color.valueOf("#2c3e50"));
        
        Text subtitle = new Text("Join Roadster to manage traffic operations");
        subtitle.setFont(Font.font("Segoe UI", 14));
        subtitle.setFill(Color.valueOf("#7f8c8d"));
        
        header.getChildren().addAll(title, subtitle);
        
        // Form fields with reduced spacing
        VBox formFields = new VBox(15);
        
        // Full name field
        VBox fullNameGroup = createFormGroup("Full Name", fullNameField, "👤");
        
        // Email field
        VBox signupEmailGroup = createFormGroup("Email Address", signupEmailField, "📧");
        
        // Password field
        VBox signupPasswordGroup = createFormGroup("Password", signupPasswordField, "🔒");
        
        // Confirm password field
        VBox confirmPasswordGroup = createFormGroup("Confirm Password", confirmPasswordField, "🔒");
        
        // Role selection
        VBox roleGroup = createFormGroup("Role", roleComboBox, "🏷️");
        
        // Terms agreement with smaller spacing
        agreeTermsCheckBox.setWrapText(true);
        agreeTermsCheckBox.setPrefHeight(40);
        
        formFields.getChildren().addAll(
            fullNameGroup, signupEmailGroup, signupPasswordGroup, 
            confirmPasswordGroup, roleGroup, agreeTermsCheckBox
        );
        
        // Signup button
        signupButton.setMaxWidth(Double.MAX_VALUE);
        signupButton.setPrefHeight(45);
        
        // Toggle to login
        Button toggleToLoginButton = new Button("Already have an account? Sign in here");
        toggleToLoginButton.setMaxWidth(Double.MAX_VALUE);
        toggleToLoginButton.getStyleClass().add("toggle-link");
        toggleToLoginButton.setOnAction(e -> showLoginForm());
        
        form.getChildren().addAll(header, formFields, signupButton, toggleToLoginButton);
        return form;
    }
    
    private VBox createFormGroup(String labelText, Control control, String icon) {
        VBox group = new VBox(6);
        
        Label label = new Label(labelText);
        label.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        label.setTextFill(Color.valueOf("#2c3e50"));
        
        HBox inputContainer = new HBox(12);
        inputContainer.setAlignment(Pos.CENTER_LEFT);
        inputContainer.getStyleClass().add("input-container");
        
        Text iconText = new Text(icon);
        iconText.setFont(Font.font(16));
        iconText.setFill(Color.valueOf("#95a5a6"));
        
        control.setPrefHeight(40);
        control.setMaxWidth(Double.MAX_VALUE);
        control.getStyleClass().add("form-input");
        
        inputContainer.getChildren().addAll(iconText, control);
        HBox.setHgrow(control, Priority.ALWAYS);
        
        group.getChildren().addAll(label, inputContainer);
        return group;
    }
    
    private void setupStyling() {
        getStyleClass().add("login-view");
        
        // Apply gradient background
        setStyle("-fx-background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);");
    }
    
    private void setupEventHandlers() {
        // Toggle between login and signup forms
        toggleFormButton.setOnAction(e -> showSignupForm());
        
        // Login button action
        loginButton.setOnAction(e -> handleLogin());
        
        // Signup button action
        signupButton.setOnAction(e -> handleSignup());
    }
    
    private void showLoginForm() {
        loginForm.setVisible(true);
        signupForm.setVisible(false);
    }
    
    private void showSignupForm() {
        loginForm.setVisible(false);
        signupForm.setVisible(true);
    }
    
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();
        boolean rememberMe = rememberMeCheckBox.isSelected();
        
        // TODO: Implement login validation and authentication
        System.out.println("Login attempt: " + email + " (Remember: " + rememberMe + ")");
        
        // For now, just show success message
        showAlert("Login", "Login successful!", Alert.AlertType.INFORMATION);
    }
    
    private void handleSignup() {
        String fullName = fullNameField.getText();
        String email = signupEmailField.getText();
        String password = signupPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String role = roleComboBox.getValue();
        boolean agreeTerms = agreeTermsCheckBox.isSelected();
        
        // TODO: Implement signup validation
        if (!validateSignup(fullName, email, password, confirmPassword, role, agreeTerms)) {
            return;
        }
        
        System.out.println("Signup attempt: " + fullName + " (" + email + ") - Role: " + role);
        
        // For now, just show success message
        showAlert("Signup", "Account created successfully!", Alert.AlertType.INFORMATION);
        showLoginForm(); // Switch back to login form
    }
    
    private boolean validateSignup(String fullName, String email, String password, 
                                 String confirmPassword, String role, boolean agreeTerms) {
        if (fullName.trim().isEmpty()) {
            showAlert("Validation Error", "Full name is required", Alert.AlertType.ERROR);
            return false;
        }
        
        if (email.trim().isEmpty() || !email.contains("@")) {
            showAlert("Validation Error", "Valid email is required", Alert.AlertType.ERROR);
            return false;
        }
        
        if (password.length() < 6) {
            showAlert("Validation Error", "Password must be at least 6 characters", Alert.AlertType.ERROR);
            return false;
        }
        
        if (!password.equals(confirmPassword)) {
            showAlert("Validation Error", "Passwords do not match", Alert.AlertType.ERROR);
            return false;
        }
        
        if ("Select your role".equals(role)) {
            showAlert("Validation Error", "Please select a role", Alert.AlertType.ERROR);
            return false;
        }
        
        if (!agreeTerms) {
            showAlert("Validation Error", "You must agree to the terms", Alert.AlertType.ERROR);
            return false;
        }
        
        return true;
    }
    
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 