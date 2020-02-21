package com.example.YogaRestAPI.service;

import com.example.YogaRestAPI.domain.Account;
import com.example.YogaRestAPI.domain.Role;
import com.example.YogaRestAPI.domain.User;
import com.example.YogaRestAPI.repos.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepo accountRepo;
    private final UserService userService;

    @Autowired
    public AccountService(AccountRepo accountRepo, UserService userService) {
        this.accountRepo = accountRepo;
        this.userService = userService;
    }
    public List<Account> findAll() {
        return accountRepo.findAll();
    }

    public void saveAccount(Account account){
        account.getRoles().add(Role.ROLE_USER);
        accountRepo.save(account);
    }

    public void deleteById(Long id) {
        accountRepo.deleteById(id);
    }

    public Account findById(Long id){
        return accountRepo.findById(id).orElse(null);
    }
}
