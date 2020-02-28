package com.example.YogaRestAPI.models;

import com.example.YogaRestAPI.domain.Account;
import com.example.YogaRestAPI.domain.User;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;

@Relation(value = "user", collectionRelation = "users")
public class UserModel extends RepresentationModel<UserModel> {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phoneNumber;
    private final LocalDate birth;
    private final Account account;

    public UserModel(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.birth = user.getBirth();
        this.account = user.getAccount();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public Account getAccount() {
        return account;
    }
}
