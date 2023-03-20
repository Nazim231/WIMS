package models;

public class EmployeeDetails {
    int id;
    String name;
    String email;

    public EmployeeDetails(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getEmpID() {
        return id;
    }

    public String getEmpName() {
        return name;
    }

    public String getEmpEmail() {
        return email;
    }
}
