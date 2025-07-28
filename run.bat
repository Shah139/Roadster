@echo off
echo Roadster JavaFX Demo Application
echo ================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install JDK 17 or higher from https://adoptium.net/
    pause
    exit /b 1
)

REM Check if Maven is installed
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven from https://maven.apache.org/download.cgi
    pause
    exit /b 1
)

echo Building and running the application...
echo.

REM Clean and compile
echo Step 1: Compiling the project...
mvn clean compile
if %errorlevel% neq 0 (
    echo ERROR: Compilation failed
    pause
    exit /b 1
)

echo.
echo Step 2: Running the application...
echo.

REM Run the application
mvn javafx:run

echo.
echo Application closed.
pause 