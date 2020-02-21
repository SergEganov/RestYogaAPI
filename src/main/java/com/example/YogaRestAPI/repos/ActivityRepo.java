package com.example.YogaRestAPI.repos;


import com.example.YogaRestAPI.domain.Activity;
import com.example.YogaRestAPI.domain.Lounge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ActivityRepo extends JpaRepository<Activity, Long> {
    List<Activity> findByLoungeAndDateOfActivityOrderByStartTimeAsc(Lounge lounge, LocalDate localDate);

}
