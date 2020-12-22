package com.fudan.project.data.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class ProjectDetailsResponse {

    private String capacity;
    private String activityName;
    private String activityVenue;
    private String introduction;
    private String picture;
    private boolean enrolled;
    private int status;

    private long activityStartTime;
    private long activityEndTime;

    private long signUpStartTime;
    private long signUpEndTime;

    private List<Map<String, String>> comments;
    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public List<Map<String, String>> getComments() {
        return comments;
    }

    public void setComments(List<Map<String, String>> comments) {
        this.comments = comments;
    }


    public long getActivityEndTime() {
        return activityEndTime;
    }

    public void setActivityEndTime(long activityEndTime) {
        this.activityEndTime = activityEndTime;
    }


    public long getSignUpEndTime() {
        return signUpEndTime;
    }

    public void setSignUpEndTime(long signUpEndTime) {
        this.signUpEndTime = signUpEndTime;
    }

    public long getActivityStartTime() {
        return activityStartTime;
    }

    public void setActivityStartTime(long activityStartTime) {
        this.activityStartTime = activityStartTime;
    }

    public long getSignUpStartTime() {
        return signUpStartTime;
    }

    public void setSignUpStartTime(long signUpStartTime) {
        this.signUpStartTime = signUpStartTime;
    }

    public String getActivityVenue() {
        return activityVenue;
    }

    public void setActivityVenue(String activityVenue) {
        this.activityVenue = activityVenue;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public boolean getEnrolled() {
        return enrolled;
    }

    public void setEnrolled(boolean enrolled) {
        this.enrolled = enrolled;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
