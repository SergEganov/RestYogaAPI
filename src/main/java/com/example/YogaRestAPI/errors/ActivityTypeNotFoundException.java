package com.example.YogaRestAPI.errors;

public class ActivityTypeNotFoundException extends RuntimeException {
    public ActivityTypeNotFoundException(Long id) {
        super("Activity type id not found^ " + id);
    }
}
