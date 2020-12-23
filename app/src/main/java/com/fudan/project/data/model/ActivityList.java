package com.fudan.project.data.model;

import java.util.ArrayList;

public class ActivityList {
    private Activity[] activities;
    public ActivityList(Activity[] activities){
        this.activities = activities;
    }

    public Activity[] getActivities() {
        return activities;
    }
}
