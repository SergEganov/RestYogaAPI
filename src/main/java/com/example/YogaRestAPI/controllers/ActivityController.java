package com.example.YogaRestAPI.controllers;

import com.example.YogaRestAPI.assemblers.ActivityModelAssembler;
import com.example.YogaRestAPI.assemblers.UserModelAssembler;
import com.example.YogaRestAPI.domain.Activity;
import com.example.YogaRestAPI.domain.User;
import com.example.YogaRestAPI.errors.Activity.ActivityNotFoundException;
import com.example.YogaRestAPI.errors.ActivityType.ActivityTypeNotFoundException;
import com.example.YogaRestAPI.models.ActivityModel;
import com.example.YogaRestAPI.models.UserModel;
import com.example.YogaRestAPI.service.ActivityService;
import com.example.YogaRestAPI.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/activities", produces = "application/json")
@CrossOrigin(origins = "*")
public class ActivityController {

    private final ActivityService activityService;
    private final UserService userService;

    @Autowired
    public ActivityController(ActivityService activityService, UserService userService) {
        this.activityService = activityService;
        this.userService = userService;
    }

    @ApiOperation("Получить список занятий")
    @GetMapping
    public ResponseEntity<Object> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                  @RequestParam(value = "size", defaultValue = "8") Integer size) {
        Page<Activity> activitiesPaged = activityService.findAllPaginated(page, size);
        CollectionModel<ActivityModel> activities = new ActivityModelAssembler().toCollectionModel(activitiesPaged);
        activities.add(linkTo(methodOn(ActivityController.class).findAll(page, size)).withRel("activities"));
        return new ResponseEntity<>(activities, HttpStatus.OK);
    }

    @ApiOperation("Найти занятие по id")
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") Long id) {
        Activity activity = activityService.findById(id).orElseThrow(() -> new ActivityNotFoundException(id));
        ActivityModel activityModel = new ActivityModel(activity);
        activityModel.add(linkTo(methodOn(ActivityController.class).findById(id)).withRel("activity"));
        return new ResponseEntity<>(activityModel, HttpStatus.OK);
    }

    @ApiOperation("Создать новое занятие")
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Object> createNew(@RequestBody Activity activity) {
        ActivityModel activityModel = new ActivityModel(activityService.save(activity));
        activityModel.add(linkTo(methodOn(ActivityController.class).findById(activity.getId())).withRel("activity"));
        return new ResponseEntity<>(activityModel, HttpStatus.CREATED);
    }

    @ApiOperation("Обновить занятие или создать новое, если такого нет")
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

    @ApiOperation("Обновить занятие")
    @PatchMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<Object> patch(@RequestBody Activity patch, @PathVariable Long id) {
        Activity activity = activityService.findById(id).orElseThrow(() -> new ActivityTypeNotFoundException(id));
        ActivityModel activityModel = new ActivityModel(activityService.save(activityService.patch(patch, activity)));
        activityModel.add(linkTo(methodOn(ActivityController.class).patch(patch, id)).withRel("activity"));
        return new ResponseEntity<>(activityModel, HttpStatus.OK);
    }

    @ApiOperation("Удалить занятие")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        activityService.deleteById(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @ApiOperation("Записаться на занятие")
    @PostMapping(value = "/{id}")
    public ResponseEntity<Object> signUp(@PathVariable("id") Long id,
                                         @Valid User user) {
        Optional<Activity> activityFromDb = activityService.findById(id);
        if (activityFromDb.isPresent()) {
            activityService.checkForSignUp(activityFromDb.get(), user);
            activityService.signUpToActivity(activityFromDb.get(), user);
            ActivityModel activityModel = new ActivityModel(activityFromDb.get());
            activityModel.add(linkTo(methodOn(ActivityController.class).signUp(id, user)).withRel("activity"));
            return new ResponseEntity<>(activityModel,HttpStatus.OK);
        } else {
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation("Получить список записанных на занятие клиентов")
    @GetMapping("/{id}/users")
    public ResponseEntity<Object> getParticipantsList(@PathVariable("id") Long id) {
        Activity activity = activityService.findById(id).orElseThrow(() -> new ActivityNotFoundException(id));
        Set<User> users = activity.getUsers();
        CollectionModel<UserModel> usersModel = new UserModelAssembler().toCollectionModel(users);
        usersModel.add(linkTo(methodOn(ActivityController.class).getParticipantsList(id)).withRel("activities"));
        return new ResponseEntity<>(usersModel, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation("Отменить запись на занятие")
    @PostMapping(value = "/{id}/users")
    public ResponseEntity<Object> signOut(@PathVariable("id") Long id,
                                         @RequestParam("userId") Long userId) {
        Optional<Activity> activityFromDb = activityService.findById(id);
        if (activityFromDb.isPresent()) {
            activityService.signOutFromActivity(activityFromDb.get(), userId);
            ActivityModel activityModel = new ActivityModel(activityFromDb.get());
            activityModel.add(linkTo(methodOn(ActivityController.class).signOut(id, userId)).withRel("activities"));
            return new ResponseEntity<>(activityModel,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
