#!/bin/bash

echo "Roadster JavaFX Demo Application"
echo "================================"
echo

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed or not in PATH"
    echo "Please install JDK 17 or higher from https://adoptium.net/"
    exit 1
fi

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven is not installed or not in PATH"
    echo "Please install Maven from https://maven.apache.org/download.cgi"
    exit 1
fi

echo "Building and running the application..."
echo

# Clean and compile
echo "Step 1: Compiling the project..."
mvn clean compile
if [ $? -ne 0 ]; then
    echo "ERROR: Compilation failed"
    exit 1
fi

echo
echo "Step 2: Running the application..."
echo

# Run the application
mvn javafx:run

echo
echo "Application closed." 