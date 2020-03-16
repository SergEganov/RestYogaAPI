package com.example.YogaRestAPI.errors.User;

public class UserAlreadyRegistredException extends RuntimeException {
    public UserAlreadyRegistredException(String email) {
        super("User with email:[ " + email + " ] already registered! Use your login and password to enter.");
    }
}
