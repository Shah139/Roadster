# Roadster JavaFX Demo Application

A modern JavaFX application demonstrating car inventory management with a clean, professional UI design.

## Features

- **Car Inventory Management**: Add, update, delete, and view car information
- **Modern UI**: Clean, responsive design with professional styling
- **Data Validation**: Real-time form validation with error handling
- **Table View**: Sortable table displaying car information
- **Sample Data**: Pre-loaded with sample car data for demonstration

## Prerequisites

Before running this application, ensure you have:

1. **Java Development Kit (JDK) 17 or higher**
   - Download from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://adoptium.net/)
   - Verify installation: `java -version`

2. **Maven 3.6 or higher**
   - Download from [Apache Maven](https://maven.apache.org/download.cgi)
   - Verify installation: `mvn -version`

3. **IDE (Optional but recommended)**
   - IntelliJ IDEA
   - Eclipse with JavaFX plugin
   - VS Code with Java extensions

## Project Structure

```
Roadster/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── roadster/
│   │   │           ├── Main.java                 # Main application class
│   │   │           ├── controllers/
│   │   │           │   └── MainController.java   # UI controller
│   │   │           └── models/
│   │   │               └── Car.java              # Car data model
│   │   └── resources/
│   │       ├── fxml/
│   │       │   └── main.fxml                     # UI layout
│   │       └── css/
│   │           └── styles.css                    # Application styling
├── pom.xml                                        # Maven configuration
└── README.md                                      # This file
```

## Getting Started

### 1. Clone or Download the Project

If you're starting from scratch (as this is an empty workspace), all files have been created for you.

### 2. Build the Project

Open a terminal in the project root directory and run:

```bash
mvn clean compile
```

### 3. Run the Application

#### Option 1: Using Maven (Recommended)
```bash
mvn javafx:run
```

#### Option 2: Using Java directly
```bash
mvn clean package
java -jar target/roadster-demo-1.0.0.jar
```

#### Option 3: From IDE
- Open the project in your IDE
- Run the `Main.java` class directly

## Usage

### Adding a Car
1. Fill in the car details in the form on the right panel
2. Click "Add Car" button
3. The car will appear in the table

### Updating a Car
1. Select a car from the table
2. Modify the details in the form
3. Click "Update Car" button

### Deleting a Car
1. Select a car from the table
2. Click "Delete Car" button
3. Confirm the deletion in the dialog

### Clearing the Form
- Click "Clear Form" to reset all form fields

## Application Features

### Car Properties
- **Brand**: Car manufacturer (e.g., Tesla, BMW)
- **Model**: Car model name (e.g., Model S, i4)
- **Year**: Manufacturing year
- **Price**: Car price in dollars
- **Color**: Car color
- **Available**: Whether the car is available for purchase

### UI Features
- **Responsive Design**: Adapts to window resizing
- **Form Validation**: Real-time input validation
- **Status Updates**: Shows operation status and feedback
- **Professional Styling**: Modern, clean interface
- **Table Sorting**: Click column headers to sort

## Technical Details

### JavaFX Version
- Uses JavaFX 17.0.2
- Compatible with JDK 17+

### Architecture
- **MVC Pattern**: Model-View-Controller architecture
- **FXML**: UI layout defined in XML
- **CSS Styling**: Custom CSS for modern appearance
- **Property Binding**: JavaFX properties for reactive UI

### Dependencies
- JavaFX Controls
- JavaFX FXML
- JavaFX Graphics
- JavaFX Base

## Troubleshooting

### Common Issues

1. **"JavaFX runtime components are missing"**
   - Ensure you're using JDK 17+ or have OpenJFX installed
   - Use `mvn javafx:run` instead of running the JAR directly

2. **"Module not found"**
   - Make sure you're using the correct Java version
   - Clean and rebuild: `mvn clean compile`

3. **UI not loading**
   - Check that all FXML and CSS files are in the correct resource directories
   - Verify the controller class path in the FXML file

### Build Issues

If you encounter build issues:

```bash
# Clean and rebuild
mvn clean compile

# Check Java version
java -version

# Check Maven version
mvn -version
```

## Customization

### Adding New Features
1. **New Car Properties**: Modify `Car.java` model
2. **UI Changes**: Edit `main.fxml` layout
3. **Styling**: Update `styles.css`
4. **Logic**: Extend `MainController.java`

### Styling
The application uses custom CSS for styling. Key style classes:
- `.main-container`: Main application container
- `.primary-button`: Primary action buttons
- `.secondary-button`: Secondary action buttons
- `.danger-button`: Delete/destructive actions
- `.table-view`: Data table styling

## License

This is a demo application created for educational purposes.

## Support

For issues or questions:
1. Check the troubleshooting section above
2. Verify your Java and Maven versions
3. Ensure all dependencies are properly installed

---

**Enjoy exploring the Roadster JavaFX Demo!** 🚗 