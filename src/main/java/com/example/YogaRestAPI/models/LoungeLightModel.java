package com.example.YogaRestAPI.models;

import com.example.YogaRestAPI.assemblers.ActivityTypeModelAssembler;
import com.example.YogaRestAPI.domain.Lounge;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalTime;

@Relation(value = "lounge", collectionRelation = "lounges")
public class LoungeLightModel extends RepresentationModel<LoungeLightModel> {
    private static final ActivityTypeModelAssembler actTypeModelAssembler = new ActivityTypeModelAssembler();

    
    //Исключен список мероприятий. JsonView не работает с CollectionModel(((((
    private final String name;

    private final String address;

    private final int capacity;

    private final LocalTime startTime;

    private final LocalTime finishTime;

    private final Iterable<ActivityTypeModel> activityTypes;

    public LoungeLightModel(Lounge lounge) {
        this.name = lounge.getName();
        this.address = lounge.getAddress();
        this.capacity = lounge.getCapacity();
        this.startTime = lounge.getStartTime();
        this.finishTime = lounge.getFinishTime();
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

    public Iterable<ActivityTypeModel> getActivityTypes() {
        return activityTypes;
    }
}
