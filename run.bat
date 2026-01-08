@echo off
REM Hospital Management System - Windows Run Script

echo ==========================================
echo Hospital Management System - Startup Guide
echo ==========================================
echo.

REM Check Java version
echo 1. Checking Java installation...
java -version
echo.

REM Build the project
echo 2. Building the project with Maven...
call mvn clean compile -DskipTests
echo.

REM Download dependencies
echo 3. Downloading MySQL JDBC driver and dependencies...
call mvn dependency:copy-dependencies -DoutputDirectory=target\dependency
echo.

REM Run the application
echo 4. Starting Hospital Management System...
echo.
java -cp "target\classes;target\dependency\*" org.example.Main
echo.
pause
