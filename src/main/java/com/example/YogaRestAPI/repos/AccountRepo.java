package com.example.YogaRestAPI.repos;

import com.example.YogaRestAPI.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepo extends JpaRepository<Account, Long> {
    Account findByLogin(String login);
}
