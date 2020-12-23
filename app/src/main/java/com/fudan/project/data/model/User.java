package com.fudan.project.data.model;

public class User {
    private String username;
    private int userId;
    private String avatar;
    private String introduction;

    public String getWorkNumber() {
        return workNumber;
    }

    private String workNumber;
    public User(){}

    public User(String username, int userId, String avatar, String introduction) {
        this.avatar = avatar;
        this.introduction = introduction;
        this.userId = userId;
        this.username = username;
    }
    public int getUserId() {
        return userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getUsername() {
        return username;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
