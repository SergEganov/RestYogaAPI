package com.example.YogaRestAPI.errors.User;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long id) {
        super("User with id:" + id + " not found!");
    }
}
