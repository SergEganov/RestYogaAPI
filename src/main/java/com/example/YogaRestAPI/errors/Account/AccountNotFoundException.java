package com.example.YogaRestAPI.errors.Account;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String login) {
        super("Account with login:[" + login + "] not found.");
    }
}
