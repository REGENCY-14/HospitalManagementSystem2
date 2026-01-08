package dao;

import model.Department;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {
    
    /**
     * Adds a new department
     */
    public static boolean addDepartment(Department department) {
        String query = "INSERT INTO Department (name, location) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, department.getName());
            stmt.setString(2, department.getLocation());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding department: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Retrieves a department by ID
     */
    public static Department getDepartmentById(int departmentId) {
        String query = "SELECT * FROM Department WHERE department_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, departmentId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Department(
                    rs.getInt("department_id"),
                    rs.getString("name"),
                    rs.getString("location")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving department: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Retrieves all departments
     */
    public static List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();
        String query = "SELECT * FROM Department";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                departments.add(new Department(
                    rs.getInt("department_id"),
                    rs.getString("name"),
                    rs.getString("location")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all departments: " + e.getMessage());
        }
        return departments;
    }
    
    /**
     * Updates a department
     */
    public static boolean updateDepartment(Department department) {
        String query = "UPDATE Department SET name = ?, location = ? WHERE department_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, department.getName());
            stmt.setString(2, department.getLocation());
            stmt.setInt(3, department.getDepartmentId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating department: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Deletes a department
     */
    public static boolean deleteDepartment(int departmentId) {
        String query = "DELETE FROM Department WHERE department_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, departmentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting department: " + e.getMessage());
            return false;
        }
    }
}
