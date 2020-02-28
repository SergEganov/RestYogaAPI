package com.example.YogaRestAPI.models;

import com.example.YogaRestAPI.assemblers.ActivityLightModelAssembler;
import com.example.YogaRestAPI.assemblers.ActivityTypeModelAssembler;
import com.example.YogaRestAPI.domain.Lounge;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalTime;

@Relation(value = "lounge", collectionRelation = "lounges")
public class LoungeModel extends RepresentationModel<LoungeModel> {

    private static final ActivityLightModelAssembler actAssembler = new ActivityLightModelAssembler();
    private static final ActivityTypeModelAssembler actTypeModelAssembler = new ActivityTypeModelAssembler();

    private final String name;

    private final String address;

    private final int capacity;

    private final LocalTime startTime;

    private final LocalTime finishTime;

    private final Iterable<ActivityLightModel> activities;

    private final Iterable<ActivityTypeModel> activityTypes;

    public LoungeModel(Lounge lounge) {
        this.name = lounge.getName();
        this.address = lounge.getAddress();
        this.capacity = lounge.getCapacity();
        this.startTime = lounge.getStartTime();
        this.finishTime = lounge.getFinishTime();
        this.activities = actAssembler.toCollectionModel(lounge.getActivities());
        this.activityTypes = actTypeModelAssembler.toCollectionModel(lounge.getActivityTypes());
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getCapacity() {
        return capacity;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getFinishTime() {
        return finishTime;
    }

    public Iterable<ActivityLightModel> getActivities() {
        return activities;
    }

    public Iterable<ActivityTypeModel> getActivityTypes() {
        return activityTypes;
    }
}

