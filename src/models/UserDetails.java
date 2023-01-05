package models;

public class UserDetails {
    String role;
    int loginStatus;

    public UserDetails(int loginStatus) {
        this.loginStatus = loginStatus;
    }

    public UserDetails (int loginStatus, String role) {
        this.loginStatus = loginStatus;
        this.role = role;
    }

    public int getLoginStatus() {
        return loginStatus;
    }

    public String getRole() {
        return role;
    }
}
