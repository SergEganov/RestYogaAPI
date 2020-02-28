package com.example.YogaRestAPI.assemblers;

import com.example.YogaRestAPI.controllers.ActivityController;
import com.example.YogaRestAPI.domain.Activity;
import com.example.YogaRestAPI.models.ActivityModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

public class ActivityModelAssembler extends RepresentationModelAssemblerSupport <Activity, ActivityModel> {

    public ActivityModelAssembler() {
        super(ActivityController.class, ActivityModel.class);
    }

    @Override
    protected ActivityModel instantiateModel(Activity entity) {
        return new ActivityModel(entity);
    }

    @Override
    public ActivityModel toModel(Activity entity) {
        return createModelWithId(entity.getId(), entity);
    }
}
