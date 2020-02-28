package com.example.YogaRestAPI.errors.Activity;

public class ActivityNotFoundException extends RuntimeException{
    public ActivityNotFoundException(Long id) {
        super("Activity with id:" + id + " not found.");
    }
}
