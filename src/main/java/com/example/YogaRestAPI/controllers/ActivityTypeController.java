package com.example.YogaRestAPI.controllers;

import com.example.YogaRestAPI.domain.ActivityType;
import com.example.YogaRestAPI.errors.ActivityTypeNotFoundException;
import com.example.YogaRestAPI.service.ActivityTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/activity-types", produces = "application/json")
@CrossOrigin(origins = "*")
public class ActivityTypeController {

    final ActivityTypeService activityTypeService;

    @Autowired
    public ActivityTypeController(ActivityTypeService activityTypeService) {
        this.activityTypeService = activityTypeService;
    }

    @GetMapping
    public CollectionModel<EntityModel<ActivityType>> findAll() {
        List<ActivityType> activityTypesFromDb =  activityTypeService.findAll();
        CollectionModel<EntityModel<ActivityType>> activityTypes = CollectionModel.wrap(activityTypesFromDb);
        activityTypes.add(
                ControllerLinkBuilder
        );
        return activityTypes;
    }

    @GetMapping("/{id}")
    public ActivityType findActivityTypeById(@PathVariable("id") Long id) {
        return activityTypeService.findById(id).orElseThrow(() -> new ActivityTypeNotFoundException(id));
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ActivityType newActivityType(@RequestBody ActivityType activityType) {
        return activityTypeService.saveActivityType(activityType);
    }

    @PutMapping("/{id}")
    public ActivityType saveOrUpdate(@RequestBody ActivityType newActivityType, @PathVariable Long id) {

        return activityTypeService.findById(id)
                .map(actType -> {
                    actType.setName(newActivityType.getName());
                    return activityTypeService.saveActivityType(actType);
                })
                .orElseGet(() -> {
                    newActivityType.setAvailable(true);
                    return activityTypeService.saveActivityType(newActivityType);
                });
    }

    @PatchMapping(path = "/{id}", consumes = "application/json")
    public ActivityType patch(@RequestBody ActivityType patch, @PathVariable Long id) {
        ActivityType activityType = activityTypeService.findById(id).orElseThrow(() -> new ActivityTypeNotFoundException(id));
        if (patch.getName() != null) {
            activityType.setName(patch.getName());
        }
        if (patch.getAvailable() != null) {
            activityType.setAvailable(patch.getAvailable());
        }
        return activityTypeService.saveActivityType(activityType);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteActivityType(@PathVariable Long id) {
        activityTypeService.deleteById(id);
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

}
