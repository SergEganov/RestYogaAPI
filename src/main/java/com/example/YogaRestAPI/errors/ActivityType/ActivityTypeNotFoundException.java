package com.example.YogaRestAPI.errors.ActivityType;

public class ActivityTypeNotFoundException extends RuntimeException {
    public ActivityTypeNotFoundException(Long id) {
        super("Activity type with id:[" + id + "] not found.");
    }
}
