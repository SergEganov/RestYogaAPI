package com.example.YogaRestAPI.service;

import com.example.YogaRestAPI.domain.Account;
import com.example.YogaRestAPI.domain.Role;
import com.example.YogaRestAPI.domain.User;
import com.example.YogaRestAPI.errors.Account.AccountAlreadyExistsException;
import com.example.YogaRestAPI.errors.Account.AccountNotFoundException;
import com.example.YogaRestAPI.errors.User.UserAlreadyRegistredException;
import com.example.YogaRestAPI.repos.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService implements UserDetailsService {

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

    public Account save(Account account){
        account.getRoles().add(Role.ROLE_USER);
        return accountRepo.save(account);
    }

    public boolean checkAccountExist(Account account) {
        Account accountFromDb = findByLogin(account.getLogin());
        return accountFromDb != null;
    }

    public void passwordConfirm(Account account) {
        if (!account.getPassword().equals(account.getPasswordConfirm())) {
            throw new RuntimeException("Пароли должны совпадать!");
        }
    }

    public User registration(User user) {
        Account account = user.getAccount();
        if (checkAccountExist(account)) {
            throw new AccountAlreadyExistsException(user.getEmail());
        }
        User userFromDb = userService.findByEmail(user.getEmail());
        if (userFromDb != null) {
            if (userFromDb.getAccount() != null) {
                throw new UserAlreadyRegistredException(user.getEmail());
            } else {
                userFromDb.setAccount(save(account));
                return userService.save(userFromDb);
            }
        }
        user.setAccount(save(account));
        return userService.save(user);
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

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Account account = accountRepo.findByLogin(login);
        if (account == null) {
            throw new AccountNotFoundException(login);
        }
        return account;
    }
}
