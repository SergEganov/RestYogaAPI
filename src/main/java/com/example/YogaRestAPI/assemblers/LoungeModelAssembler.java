package com.example.YogaRestAPI.assemblers;

import com.example.YogaRestAPI.controllers.LoungeController;
import com.example.YogaRestAPI.domain.Lounge;
import com.example.YogaRestAPI.models.LoungeModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

public class LoungeModelAssembler extends RepresentationModelAssemblerSupport<Lounge, LoungeModel> {

    public LoungeModelAssembler(){
        super(LoungeController.class, LoungeModel.class);
    }

    @Override
    protected LoungeModel instantiateModel(Lounge lounge) {
        return new LoungeModel(lounge);
    }

    @Override
    public LoungeModel toModel(Lounge lounge) {
        return createModelWithId(lounge.getId(), lounge);
    }

}
