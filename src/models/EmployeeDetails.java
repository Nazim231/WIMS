package models;

public class EmployeeDetails {
    int id;
    String name;

    public EmployeeDetails(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getEmpID() {
        return id;
    }

    public String getEmpName() {
        return name;
    }
}
