package com.example.YogaRestAPI.service;

import com.example.YogaRestAPI.domain.Activity;
import com.example.YogaRestAPI.domain.Role;
import com.example.YogaRestAPI.domain.User;
import com.example.YogaRestAPI.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Set;


@Service
public class UserService {

    private final UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public boolean isUsersExists() {
        return userRepo.isUsersExists();
    }

    public User findById(Long id) {
        return userRepo.findById(id).orElse(null);
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public void saveUser(User user) {
            userRepo.save(user);
    }

    public void deleteById(Long id) {
        userRepo.deleteById(id);
    }

    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public boolean checkUserExist(User user) {
        User userFromDb = userRepo.findByEmail(user.getEmail());
        return userFromDb != null;
    }

    public boolean createUserValidation(User user, BindingResult bindingResult) {
        if (checkUserExist(user)) {
            bindingResult.addError(new FieldError(
                    "user",
                    "email",
                    "User with this email: " + user.getEmail() + " is exist!"));
            return false;
        }
        return true;
    }
    public boolean updateUserValidation(User user, BindingResult bindingResult) {
        User userFromDb = userRepo.findByEmail(user.getEmail());
        if (!userFromDb.getId().equals(user.getId())) {
            bindingResult.addError(new FieldError(
                    "user",
                    "email",
                    "User with this email: " + user.getEmail() + " is exist!"));
            return false;
        }
        return true;
    }

    public boolean checkUserAlreadySigned(User user, Activity activity, BindingResult bindingResult) {
        User userFromDb = userRepo.findByEmail(user.getEmail());
        if (activity.getUsers().contains(userFromDb)) {
            bindingResult.addError(new FieldError(
                    "user",
                    "email",
                    "Activity is already have user with this email: " + user.getEmail()));
            return false;
        }
        return true;
    }
}
