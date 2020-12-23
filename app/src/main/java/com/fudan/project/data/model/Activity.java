package com.fudan.project.data.model;

public class Activity {
    //item info in list
    private String activityName;
    private String introduction;
    private String type;
    private String picture;


    private String activityId;

    //more details
    private int capacity;
    private String activityVenue;



    private long activityStartTime;
    private long activityEndTime;
    private long signUpEndTime;
    private long signUpStartTime;
    private String[] comments;
    private User host = new User();
    private int currentNum;
    private boolean enrolled;

    public String getActivityName() {
        return activityName;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getType() {
        return type;
    }

    public String getPicture() {
        return picture;
    }

    public String getActivityId() {
        return activityId;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getActivityVenue() {
        return activityVenue;
    }

    public long getActivityStartTime() {
        return activityStartTime;
    }

    public long getActivityEndTime() {
        return activityEndTime;
    }

    public long getSignUpEndTime() {
        return signUpEndTime;
    }

    public long getSignUpStartTime() {
        return signUpStartTime;
    }

    public String[] getComments() {
        return comments;
    }

    public User getHost() {
        return host;
    }

    public int getCurrentNum() {
        return currentNum;
    }

    public boolean isEnrolled() {
        return enrolled;
    }






}
