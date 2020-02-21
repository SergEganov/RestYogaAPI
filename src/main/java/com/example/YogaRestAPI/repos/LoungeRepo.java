package com.example.YogaRestAPI.repos;

import com.example.YogaRestAPI.domain.Lounge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoungeRepo extends JpaRepository<Lounge, Long> {
    Lounge findByName(String name);
}
