package com.example.YogaRestAPI.service;


import com.example.YogaRestAPI.domain.ActivityType;
import com.example.YogaRestAPI.repos.ActivityTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

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

    public ActivityType saveActivityType(ActivityType activityType){
        return activityTypeRepo.save(activityType);
    }

    public void deleteById(Long id) {
        activityTypeRepo.deleteById(id);
    }

    public Optional<ActivityType> findById(Long id){
        return activityTypeRepo.findById(id);
    }

    private Boolean checkActivityTypeExist(ActivityType activityType) {
        ActivityType activityTypeFromDb = activityTypeRepo.findByName(activityType.getName());
        return activityTypeFromDb != null;
    }

    public boolean activityTypeValidation(ActivityType activityType, BindingResult bindingResult) {
        if (checkActivityTypeExist(activityType)) {
            bindingResult.addError(new FieldError(
                    "activityType",
                    "name",
                    "Activity type with name: " + activityType.getName() + " is exist!"));
            return false;
        }
        return true;
    }

    public Page<ActivityType> findAllPaginated(Integer pageNumber) {
        int pageSize = 3;
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return activityTypeRepo.findAll(page);
    }
}
