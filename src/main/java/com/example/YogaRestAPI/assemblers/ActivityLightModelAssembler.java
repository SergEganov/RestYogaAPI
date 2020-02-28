package com.example.YogaRestAPI.assemblers;

import com.example.YogaRestAPI.controllers.ActivityController;
import com.example.YogaRestAPI.domain.Activity;
import com.example.YogaRestAPI.models.ActivityLightModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

public class ActivityLightModelAssembler extends RepresentationModelAssemblerSupport<Activity, ActivityLightModel> {
    public ActivityLightModelAssembler() {
        super(ActivityController.class, ActivityLightModel.class);
    }

    @Override
    protected ActivityLightModel instantiateModel(Activity entity) {
        return new ActivityLightModel(entity);
    }

    @Override
    public ActivityLightModel toModel(Activity entity) {
        return createModelWithId(entity.getId(), entity);
    }
}
