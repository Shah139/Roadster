@echo off
echo Roadster JavaFX Demo - Maven Build and Run
echo ==========================================
echo.

REM Use full path to Maven
set MAVEN_HOME=D:\maven\apache-maven-3.9.11
set PATH=%MAVEN_HOME%\bin;%PATH%

echo Using Maven from: %MAVEN_HOME%
echo.

REM Clean and compile
echo Step 1: Cleaning and compiling...
call mvn clean compile dependency:copy-dependencies
if %errorlevel% neq 0 (
    echo ERROR: Compilation failed
    pause
    exit /b 1
)

echo.
echo Step 2: Running JavaFX application...
echo.

REM Run the application
call mvn javafx:run

echo.
echo Application closed.
pause 