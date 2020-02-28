package com.example.YogaRestAPI.service;

import com.example.YogaRestAPI.domain.Account;
import com.example.YogaRestAPI.domain.Activity;
import com.example.YogaRestAPI.domain.User;
import com.example.YogaRestAPI.repos.UserRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    private final Integer pageSize = 2;

    private final UserRepo userRepo;
    private final AccountService accountService;

    public UserService(UserRepo userRepo, AccountService accountService) {
        this.userRepo = userRepo;
        this.accountService = accountService;
    }

    public boolean isUsersExists() {
        return userRepo.isUsersExists();
    }

    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public Page<User> findAllPaginated(Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return userRepo.findAll(pageable);
    }

    public User save(User user) {
        return userRepo.save(user);
    }

    public void deleteById(Long id) {
        userRepo.deleteById(id);
    }

    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public User patch(User patch, User target) {
        if (patch.getFirstName() != null) {
            target.setFirstName(patch.getFirstName());
        }
        if (patch.getLastName() != null) {
            target.setLastName(patch.getLastName());
        }
        if (patch.getEmail() != null) {
            target.setEmail(patch.getEmail());
        }
        if (patch.getPhoneNumber() != null) {
            target.setPhoneNumber(patch.getPhoneNumber());
        }
        if (patch.getBirth() != null) {
            target.setBirth(patch.getBirth());
        }
        if (patch.getAccount() != null) {
            target.setAccount(patch.getAccount());
        }
        return target;
    }

    public User registration(User user) {
        User userFromDb = findByEmail(user.getEmail());
        if (userFromDb != null) {
            if (userFromDb.getAccount() != null) {
               /* throw new AccountAlreadyCreatedException();*/
            }
        }
        Account account = user.getAccount();
        if (accountService.checkAccountExist(account)) {
            /*throw new AccountExistException(account.getLogin());*/
        }
        user.setAccount(accountService.save(account));
        return null;
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
