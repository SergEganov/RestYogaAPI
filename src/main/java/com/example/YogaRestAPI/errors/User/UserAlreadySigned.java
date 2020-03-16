package com.example.YogaRestAPI.errors.User;

public class UserAlreadySigned extends RuntimeException {
    public UserAlreadySigned(String email){ super("User with email:[ " + email + " ] is already signed.");}
}
