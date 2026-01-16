package service;

import dao.DepartmentDAO;
import model.Department;
import java.util.List;

public class DepartmentService {
    
    public static boolean createDepartment(Department department) {
        if (department == null || department.getName() == null || department.getName().isEmpty()) {
            System.err.println("Invalid department data");
            return false;
        }
        return DepartmentDAO.addDepartment(department);
    }
    
    public static Department getDepartment(int departmentId) {
        if (departmentId <= 0) {
            System.err.println("Invalid department ID");
            return null;
        }
        return DepartmentDAO.getDepartmentById(departmentId);
    }
    
    public static List<Department> getAllDepartments() {
        return DepartmentDAO.getAllDepartments();
    }
    
    public static boolean updateDepartment(Department department) {
        if (department == null || department.getDepartmentId() <= 0) {
            System.err.println("Invalid department data for update");
            return false;
        }
        return DepartmentDAO.updateDepartment(department);
    }
    
    public static boolean deleteDepartment(int departmentId) {
        if (departmentId <= 0) {
            System.err.println("Invalid department ID");
            return false;
        }
        return DepartmentDAO.deleteDepartment(departmentId);
    }
}
