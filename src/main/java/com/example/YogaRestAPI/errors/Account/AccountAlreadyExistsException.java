package com.example.YogaRestAPI.errors.Account;

public class AccountAlreadyExistsException extends RuntimeException {
    public AccountAlreadyExistsException(String login) {
        super("Account with login:[ " + login + " already registered.");

    }
}
