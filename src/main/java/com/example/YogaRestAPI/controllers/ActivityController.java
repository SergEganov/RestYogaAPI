package com.example.YogaRestAPI.controllers;

import com.example.YogaRestAPI.assemblers.ActivityModelAssembler;
import com.example.YogaRestAPI.domain.Activity;
import com.example.YogaRestAPI.errors.Activity.ActivityNotFoundException;
import com.example.YogaRestAPI.errors.ActivityType.ActivityTypeNotFoundException;
import com.example.YogaRestAPI.models.ActivityModel;
import com.example.YogaRestAPI.service.ActivityService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/activities", produces = "application/json")
@CrossOrigin(origins = "*")
public class ActivityController {

    private final ActivityService activityService;

    @Autowired
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                  @RequestParam(value = "size", defaultValue = "8") Integer size) {
        Page<Activity> activitiesPaged = activityService.findAllPaginated(page, size);
        CollectionModel<ActivityModel> activities = new ActivityModelAssembler().toCollectionModel(activitiesPaged);
        activities.add(linkTo(methodOn(ActivityController.class).findAll(page, size)).withRel("activities"));
        return new ResponseEntity<>(activities, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") Long id) {
        Activity activity = activityService.findById(id).orElseThrow(() -> new ActivityNotFoundException(id));
        ActivityModel activityModel = new ActivityModel(activity);
        activityModel.add(linkTo(methodOn(ActivityController.class).findById(id)).withRel("activity"));
        return new ResponseEntity<>(activityModel, HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Object> createNew(@RequestBody Activity activity) {

        ActivityModel activityModel = new ActivityModel(activityService.save(activity));
        activityModel.add(linkTo(methodOn(ActivityController.class).findById(activity.getId())).withRel("activity"));
        return new ResponseEntity<>(activityModel, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity saveOrUpdate(@RequestBody Activity activity, @PathVariable Long id) {
        return activityService.findById(id)
                .map(act -> {
                    BeanUtils.copyProperties(activity, act);
                    act.setId(id);
                    activityService.save(act);
                    ActivityModel activityModel = new ActivityModel(act);
                    activityModel.add(linkTo(methodOn(ActivityController.class)
                            .saveOrUpdate(activity, id))
                            .withRel("activity"));
                    return new ResponseEntity<>(activityModel, HttpStatus.OK);
                })
                .orElseGet(() -> {
                    ActivityModel activityModel = new ActivityModel(activityService.save(activity));
                    activityModel.add(linkTo(methodOn(ActivityController.class)
                            .findById(id))
                            .withRel("activity"));
                    return new ResponseEntity<>(activityModel, HttpStatus.CREATED);
                });
    }

    @PatchMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<Object> patch(@RequestBody Activity patch, @PathVariable Long id) {
        Activity activity = activityService.findById(id).orElseThrow(() -> new ActivityTypeNotFoundException(id));
        ActivityModel activityModel = new ActivityModel(activityService.save(activityService.patch(patch, activity)));
        activityModel.add(linkTo(methodOn(ActivityController.class).patch(patch, id)).withRel("activity"));
        return new ResponseEntity<>(activityModel, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        activityService.deleteById(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}