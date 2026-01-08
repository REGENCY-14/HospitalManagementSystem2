#!/bin/bash
# Hospital Management System - Run Script

echo "=========================================="
echo "Hospital Management System - Startup Guide"
echo "=========================================="
echo ""

# Check Java version
echo "1. Checking Java installation..."
java -version 2>&1 | head -1
echo ""

# Build the project
echo "2. Building the project with Maven..."
mvn clean compile -DskipTests
echo ""

# Download dependencies
echo "3. Downloading MySQL JDBC driver and dependencies..."
mvn dependency:copy-dependencies -DoutputDirectory=target/dependency
echo ""

# Run the application
echo "4. Starting Hospital Management System..."
echo ""
java -cp "target/classes:target/dependency/*" org.example.Main
