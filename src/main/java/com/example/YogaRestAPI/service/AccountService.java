package com.example.YogaRestAPI.service;

import com.example.YogaRestAPI.domain.Account;
import com.example.YogaRestAPI.domain.Role;
import com.example.YogaRestAPI.repos.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepo accountRepo;

    @Autowired
    public AccountService(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }
    public List<Account> findAll() {
        return accountRepo.findAll();
    }

    public Account save(Account account){
        account.getRoles().add(Role.ROLE_USER);
        return accountRepo.save(account);
    }

    public boolean checkAccountExist(Account account) {
        Account accountFromDb = findByLogin(account.getLogin());
        return accountFromDb != null;
    }

    public void deleteById(Long id) {
        accountRepo.deleteById(id);
    }

    public Account findById(Long id){
        return accountRepo.findById(id).orElse(null);
    }

    public Account findByLogin(String login) {
        return accountRepo.findByLogin(login);
    }
}
