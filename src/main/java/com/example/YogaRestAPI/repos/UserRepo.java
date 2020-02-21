package com.example.YogaRestAPI.repos;

import com.example.YogaRestAPI.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Query(value = "SELECT EXISTS (SELECT NULL FROM usrs)", nativeQuery = true)
    boolean isUsersExists();
}

