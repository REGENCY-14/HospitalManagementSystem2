package model;

public class Department {
    private int departmentId;
    private String name;
    private String location;

    // Constructor
    public Department(int departmentId, String name, String location) {
        this.departmentId = departmentId;
        this.name = name;
        this.location = location;
    }

    public Department(String name, String location) {
        this.name = name;
        this.location = location;
    }

    // Getters and Setters
    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Department{" +
                "departmentId=" + departmentId +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
