package com.example.YogaRestAPI.repos;

import com.example.YogaRestAPI.domain.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityTypeRepo extends JpaRepository<ActivityType, Long> {
    ActivityType findByName(String name);
}
