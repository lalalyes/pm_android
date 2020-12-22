package com.fudan.project.data.model;

import java.util.List;
import java.util.Map;

public class MyProjectResponse {

    List<Map<String, String>> activities;

    public MyProjectResponse(List<Map<String, String>> activities) {
        this.activities = activities;

    }

    public List<Map<String, String>>  getActivities() {
        return activities;
    }

    public void setActivities(List<Map<String, String>> activities) {
        this.activities = activities;
    }
}
