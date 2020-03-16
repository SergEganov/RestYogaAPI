package com.example.YogaRestAPI.service;

import com.example.YogaRestAPI.domain.Account;
import com.example.YogaRestAPI.domain.Activity;
import com.example.YogaRestAPI.domain.Role;
import com.example.YogaRestAPI.domain.User;
import com.example.YogaRestAPI.errors.Account.AccountAlreadyExistsException;
import com.example.YogaRestAPI.errors.User.UserAlreadyRegistredException;
import com.example.YogaRestAPI.errors.User.UserAlreadySigned;
import com.example.YogaRestAPI.errors.User.UserExistsException;
import com.example.YogaRestAPI.repos.UserRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    private final UserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
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
        if (user.getAccount() != null) {
            user.getAccount().getRoles().add(Role.ROLE_USER);
            user.getAccount().setPassword(passwordEncoder.encode(user.getAccount().getPassword()));
        }
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

    public boolean checkUserExist(User user) {
        User userFromDb = userRepo.findByEmail(user.getEmail());
        return userFromDb != null;
    }

    public void checkForUpdate(User user) {
        User userFromDb = userRepo.findByEmail(user.getEmail());
        if (userFromDb != null && (!userFromDb.getId().equals(user.getId()))) {
           throw new UserExistsException(user.getEmail());
        }
    }

    public void checkUserAlreadySigned(User user, Activity activity) {
        User userFromDb = userRepo.findByEmail(user.getEmail());
        if (activity.getUsers().contains(userFromDb)) {
            throw new UserAlreadySigned(user.getEmail());
        }
    }
}
