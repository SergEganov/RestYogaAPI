package com.example.YogaRestAPI.service;


import com.example.YogaRestAPI.domain.ActivityType;
import com.example.YogaRestAPI.errors.ActivityType.ActivityTypeExistsException;
import com.example.YogaRestAPI.repos.ActivityTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActivityTypeService {

    private final ActivityTypeRepo activityTypeRepo;

    @Autowired
    public ActivityTypeService(ActivityTypeRepo activityTypeRepo) {
        this.activityTypeRepo = activityTypeRepo;
    }

    public List<ActivityType> findAll() {
        return activityTypeRepo.findAll();
    }
    public ActivityType findByName(String name) {
        return activityTypeRepo.findByName(name);
    }

    public ActivityType save(ActivityType activityType){
        return activityTypeRepo.save(activityType);
    }

    public void deleteById(Long id) {
        activityTypeRepo.deleteById(id);
    }

    public Optional<ActivityType> findById(Long id){
        return activityTypeRepo.findById(id);
    }

    public void checkActivityTypeExist(ActivityType activityType) {
        ActivityType activityTypeFromDb = findByName(activityType.getName());
        if (activityTypeFromDb != null) {
            throw new ActivityTypeExistsException(activityType.getName());
        }
    }

    public void update(ActivityType activityType) {
        ActivityType activityTypeFromDb = findByName(activityType.getName());
        if (activityTypeFromDb != null && !(activityTypeFromDb.getId().equals(activityType.getId()))) {
            throw new ActivityTypeExistsException(activityType.getName());
        }
    }

    public ActivityType patch (ActivityType patch, ActivityType target) {
        if (patch.getName() != null) {
            target.setName(patch.getName());
        }
        if (patch.getAvailable() != null) {
            target.setAvailable(patch.getAvailable());
        }
        return target;
    }
}
