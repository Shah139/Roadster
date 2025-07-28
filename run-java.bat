@echo off
echo Roadster JavaFX Demo - Direct Java Launch
echo =========================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install JDK 17 or higher from https://adoptium.net/
    pause
    exit /b 1
)

echo Java found. Checking project structure...
echo.

REM Check if source files exist
if not exist "src\main\java\com\roadster\Main.java" (
    echo ERROR: Main.java not found
    echo Please ensure you're in the correct project directory
    pause
    exit /b 1
)

if not exist "src\main\java\com\roadster\controllers\MainController.java" (
    echo ERROR: MainController.java not found
    echo Please ensure all source files are present
    pause
    exit /b 1
)

echo Project structure looks good.
echo.
echo Note: This method requires JavaFX to be available.
echo If you get JavaFX errors, please install Maven and use run.bat instead.
echo.

REM Try to run directly with Java
echo Attempting to run with Java...
java -cp "src\main\java" com.roadster.Main

if %errorlevel% neq 0 (
    echo.
    echo Direct Java execution failed. This is expected without JavaFX modules.
    echo.
    echo Please install Maven to run this JavaFX application:
    echo 1. Download from: https://maven.apache.org/download.cgi
    echo 2. Extract to C:\Program Files\Apache\maven
    echo 3. Add C:\Program Files\Apache\maven\bin to PATH
    echo 4. Restart VS Code and use run.bat
    echo.
    pause
    exit /b 1
)

echo.
echo Application closed.
pause 