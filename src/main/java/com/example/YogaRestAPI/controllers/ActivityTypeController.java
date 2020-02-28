package com.example.YogaRestAPI.controllers;

import com.example.YogaRestAPI.assemblers.ActivityTypeModelAssembler;
import com.example.YogaRestAPI.domain.ActivityType;
import com.example.YogaRestAPI.errors.ActivityType.ActivityTypeNotFoundException;
import com.example.YogaRestAPI.models.ActivityTypeModel;
import com.example.YogaRestAPI.service.ActivityTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/activityTypes", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class ActivityTypeController {

    private final ActivityTypeService activityTypeService;

    @Autowired
    public ActivityTypeController(ActivityTypeService activityTypeService) {
        this.activityTypeService = activityTypeService;
    }

    @GetMapping
    public ResponseEntity<Object> findAll() {
        CollectionModel<ActivityTypeModel> activityTypes = new ActivityTypeModelAssembler().toCollectionModel(activityTypeService.findAll());
        activityTypes.add(linkTo(methodOn(ActivityTypeController.class).findAll()).withRel("activityTypes"));
        return new ResponseEntity<>(activityTypes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") Long id) {
        ActivityType activityType = activityTypeService.findById(id).orElseThrow(() -> new ActivityTypeNotFoundException(id));
        ActivityTypeModel activityTypeModel= new ActivityTypeModel(activityType);
        activityTypeModel.add(linkTo(methodOn(ActivityTypeController.class).findById(id)).withRel("activityType"));
        return new ResponseEntity<>(activityTypeModel, HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Object> createNew(@Valid @RequestBody ActivityType activityType) {
        activityTypeService.checkActivityTypeExist(activityType);
        ActivityTypeModel activityTypeModel =  new ActivityTypeModel(activityTypeService.save(activityType));
        activityTypeModel.add(linkTo(methodOn(ActivityTypeController.class).findById(activityType.getId())).withRel("activityType"));
        return new ResponseEntity<>(activityTypeModel, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity saveOrUpdate(@Valid @RequestBody ActivityType newActivityType,
                                       @PathVariable Long id) {
        return activityTypeService.findById(id)
                .map(actType -> {
                    BeanUtils.copyProperties(newActivityType, actType);
                    actType.setId(id);
                    activityTypeService.update(actType);
                    activityTypeService.save(actType);
                    ActivityTypeModel activityTypeModel = new ActivityTypeModel(actType);
                    activityTypeModel.add(linkTo(methodOn(ActivityTypeController.class)
                                    .saveOrUpdate(newActivityType,id))
                                    .withRel("activityType"));
                    return new ResponseEntity<>(activityTypeModel, HttpStatus.OK);
                })
                .orElseGet(() -> {
                    activityTypeService.checkActivityTypeExist(newActivityType);
                    ActivityType activityType = activityTypeService.save(newActivityType);
                    ActivityTypeModel model = new ActivityTypeModel(activityType);
                    model.add(linkTo(methodOn(ActivityTypeController.class)
                            .saveOrUpdate(newActivityType,activityType.getId()))
                            .withRel("activityType"));
                    return new ResponseEntity<>(model, HttpStatus.CREATED);
                });
    }

    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> patch(@Valid @RequestBody ActivityType patch,
                              @PathVariable Long id) {
        ActivityType activityType = activityTypeService.findById(id).orElseThrow(() -> new ActivityTypeNotFoundException(id));
        patch.setId(id);
        activityTypeService.update(patch);
        ActivityTypeModel activityTypeModel = new ActivityTypeModel(
                activityTypeService.save(activityTypeService.patch(patch, activityType)));

        activityTypeModel.add(linkTo(methodOn(ActivityTypeController.class).patch(activityType, id)).withRel("activityType"));
        return new ResponseEntity<>(activityTypeModel, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteActivityType(@PathVariable Long id) {
        activityTypeService.deleteById(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
