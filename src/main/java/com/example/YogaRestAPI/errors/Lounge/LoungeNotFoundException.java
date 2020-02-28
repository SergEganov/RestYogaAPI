package com.example.YogaRestAPI.errors.Lounge;

public class LoungeNotFoundException extends RuntimeException{
    public LoungeNotFoundException(Long id) {
        super("Lounge with id:[" + id + "] not found.");
    }
}
