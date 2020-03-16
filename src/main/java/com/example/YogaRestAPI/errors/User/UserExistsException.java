package com.example.YogaRestAPI.errors.User;

public class UserExistsException extends RuntimeException {
    public UserExistsException(String email) {
        super("User with email: [ " + email + " ] already exists.");
    }
}
