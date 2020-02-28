package com.example.YogaRestAPI.models;

import com.example.YogaRestAPI.domain.ActivityType;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(value = "activityType", collectionRelation = "activityTypes")
public class ActivityTypeModel extends RepresentationModel<ActivityTypeModel> {

    private final String name;

    private final Boolean available;

    public ActivityTypeModel(ActivityType activityType) {
        this.name = activityType.getName();
        this.available = activityType.getAvailable();
    }

    public String getName() {
        return name;
    }

    public Boolean getAvailable() {
        return available;
    }
}
