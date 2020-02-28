package com.example.YogaRestAPI.assemblers;

import com.example.YogaRestAPI.controllers.UserController;
import com.example.YogaRestAPI.domain.User;
import com.example.YogaRestAPI.models.UserModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

public class UserModelAssembler extends RepresentationModelAssemblerSupport<User, UserModel> {

    public UserModelAssembler() {
        super(UserController.class, UserModel.class);
    }

    @Override
    protected UserModel instantiateModel(User user) {
        return new UserModel(user);
    }

    @Override
    public UserModel toModel(User user) {
        return createModelWithId(user.getId(), user);
    }
}
