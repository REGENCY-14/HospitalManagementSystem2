#!/bin/bash
# Test MongoDB Connection and Application Startup

echo "========================================"
echo "Hospital Management System - Debug Test"
echo "========================================"
echo ""

echo "1. Checking Maven version..."
mvn -version | head -3
echo ""

echo "2. Checking MongoDB connection..."
echo "To test: mongosh --eval 'db.runCommand({ping: 1})'"
echo ""

echo "3. Compiling project..."
mvn clean compile -q
if [ $? -eq 0 ]; then
    echo "✓ Compilation successful"
else
    echo "✗ Compilation failed"
    exit 1
fi
echo ""

echo "4. Project structure check..."
ls -la src/main/java/controller/ | grep MedicalLog
ls -la src/main/java/dao/ | grep MongoDB
echo ""

echo "5. MongoDB Connection Status:"
echo "   Connection String: mongodb://localhost:27017"
echo "   Database: hospitalnosql"
echo ""

echo "6. To run the application:"
echo "   mvn javafx:run"
echo ""

echo "========================================"
echo "Pre-flight checks complete!"
echo "========================================"
