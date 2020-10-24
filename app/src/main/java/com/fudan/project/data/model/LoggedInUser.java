package com.fudan.project.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String userId;
    private String displayName;
    private String token;
    private String role;

    public LoggedInUser(String userId, String displayName, String token, String role) {
        this.userId = userId;
        this.displayName = displayName;
        this.token = token;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getRole() {
        return role;
    }

    public String getToken() {
        return token;
    }
}