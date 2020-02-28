package com.example.YogaRestAPI.assemblers;

import com.example.YogaRestAPI.controllers.LoungeController;
import com.example.YogaRestAPI.domain.Lounge;
import com.example.YogaRestAPI.models.LoungeLightModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

public class LoungeLightModelAssembler extends RepresentationModelAssemblerSupport<Lounge, LoungeLightModel> {

    public LoungeLightModelAssembler(){
        super(LoungeController.class, LoungeLightModel.class);
    }

    @Override
    protected LoungeLightModel instantiateModel(Lounge lounge) {
        return new LoungeLightModel(lounge);
    }

    @Override
    public LoungeLightModel toModel(Lounge lounge) {
        return createModelWithId(lounge.getId(), lounge);
    }

}
