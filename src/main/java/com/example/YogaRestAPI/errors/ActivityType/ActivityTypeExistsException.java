package com.example.YogaRestAPI.errors.ActivityType;

public class ActivityTypeExistsException extends RuntimeException {
    public ActivityTypeExistsException(String name) {
        super("Activity type with name:[" + name + "] is already exists.");
    }
}
