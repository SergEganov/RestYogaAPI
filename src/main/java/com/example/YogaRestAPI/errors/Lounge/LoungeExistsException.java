package com.example.YogaRestAPI.errors.Lounge;

public class LoungeExistsException extends RuntimeException{
    public LoungeExistsException(String name) {
        super("Lounge with name:[" + name + "] already exists.");
    }
}
