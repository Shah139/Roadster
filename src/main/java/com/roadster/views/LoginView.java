package com.roadster.views;

import com.roadster.controllers.MainController;
import com.roadster.utils.SharedPrefs;
import com.roadster.utils.UserStorage;
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
    private MainController mainController;

    public LoginView(MainController mainController) {
        this.mainController = mainController;
        initializeComponents();
        setupLayout();
        setupStyling();
        setupEventHandlers();
        loadSavedCredentials(); // Add this line
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

        brandingSection.getChildren().addAll(logo, welcomeText, subtitleText);
        return brandingSection;
    }

    private VBox createFormsSection() {
        // Create main container for the entire right side
        VBox formsSection = new VBox();
        formsSection.setAlignment(Pos.CENTER);
        formsSection.setPadding(new Insets(40));
        formsSection.getStyleClass().add("login-right");

        // Create a centered container that will hold the forms
        VBox formsContainer = new VBox();
        formsContainer.setAlignment(Pos.CENTER);
        formsContainer.setMaxWidth(400);
        formsContainer.setMaxHeight(Region.USE_PREF_SIZE); // Important: don't stretch vertically

        // Login form
        loginForm = createLoginForm();

        // Signup form (initially hidden)
        signupForm = createSignupForm();
        signupForm.setVisible(false);
        signupForm.setManaged(false); // Important: don't take up space when hidden

        formsContainer.getChildren().addAll(loginForm, signupForm);

        // Add the forms container to the main section
        formsSection.getChildren().add(formsContainer);

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
        VBox form = new VBox(20); // Reduced spacing from 25 to 20
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
        VBox formFields = new VBox(12); // Reduced spacing from 15 to 12

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
        agreeTermsCheckBox.setPrefHeight(35); // Reduced from 40 to 35

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
        loginForm.setManaged(true);

        signupForm.setVisible(false);
        signupForm.setManaged(false); // Important: don't take up space when hidden
    }

    private void showSignupForm() {
        signupForm.setVisible(true);
        signupForm.setManaged(true);

        loginForm.setVisible(false);
        loginForm.setManaged(false); // Important: don't take up space when hidden
    }

    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();
        boolean rememberMe = rememberMeCheckBox.isSelected();

        // First check if fields are not empty
        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Login Failed", "Please enter both email and password", Alert.AlertType.ERROR);
            return;
        }

        // Check if user is registered
        if (!UserStorage.isUserRegistered(email)) {
            showAlert("Login Failed", "No account found with this email. Please sign up first.", Alert.AlertType.ERROR);
            return;
        }

        // Validate credentials
        if (UserStorage.validateCredentials(email, password)) {
            // Save credentials if remember me is checked
            if (rememberMe) {
                SharedPrefs.saveLoginCredentials(email, password, true);
            }
            showAlert("Login", "Login successful!", Alert.AlertType.INFORMATION);
            mainController.showDashboardView();
        } else {
            showAlert("Login Failed", "Invalid email or password", Alert.AlertType.ERROR);
        }
    }

    // Add this method to initialize fields with saved credentials
    private void loadSavedCredentials() {
        if (SharedPrefs.isRememberMe()) {
            emailField.setText(SharedPrefs.getSavedEmail());
            passwordField.setText(SharedPrefs.getSavedPassword());
            rememberMeCheckBox.setSelected(true);
        }
    }

    private void handleSignup() {
        String fullName = fullNameField.getText();
        String email = signupEmailField.getText();
        String password = signupPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String role = roleComboBox.getValue();
        boolean agreeTerms = agreeTermsCheckBox.isSelected();

        // Validate signup input
        if (!validateSignup(fullName, email, password, confirmPassword, role, agreeTerms)) {
            return;
        }

        // Check if user already exists
        if (UserStorage.isUserRegistered(email)) {
            showAlert("Signup Failed", "An account with this email already exists", Alert.AlertType.ERROR);
            return;
        }

        // Register the new user
        UserStorage.registerUser(email, password, fullName, role);
        
        showAlert("Signup", "Account created successfully! Please login.", Alert.AlertType.INFORMATION);
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

