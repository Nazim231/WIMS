package models;

public class UserDetails {
    String name, role;
    int loginStatus;

    public UserDetails(int loginStatus) {
        this.loginStatus = loginStatus;
    }

    public UserDetails (int loginStatus, String name, String role) {
        this.loginStatus = loginStatus;
        this.name = name;
        this.role = role;
    }

    public int getLoginStatus() {
        return loginStatus;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }
}
