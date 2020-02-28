package com.example.YogaRestAPI.assemblers;

import com.example.YogaRestAPI.controllers.ActivityTypeController;
import com.example.YogaRestAPI.domain.ActivityType;
import com.example.YogaRestAPI.models.ActivityTypeModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

public class ActivityTypeModelAssembler extends RepresentationModelAssemblerSupport<ActivityType, ActivityTypeModel> {

    public ActivityTypeModelAssembler(){
        super(ActivityTypeController.class, ActivityTypeModel.class);
    }

    @Override
    protected ActivityTypeModel instantiateModel(ActivityType activityType) {
        return new ActivityTypeModel(activityType);
    }

    @Override
    public ActivityTypeModel toModel(ActivityType activityType) {
        return createModelWithId(activityType.getId(), activityType);
    }
}
