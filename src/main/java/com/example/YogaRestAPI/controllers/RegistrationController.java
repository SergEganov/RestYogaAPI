package com.example.YogaRestAPI.controllers;

import com.example.YogaRestAPI.domain.User;
import com.example.YogaRestAPI.models.UserModel;
import com.example.YogaRestAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/registration", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserModel createNew(@RequestBody User user) {
        UserModel userModel = new UserModel(userService.save(user));
        userModel.add(linkTo(methodOn(RegistrationController.class).createNew(user)).withRel("user"));
        return userModel;
    }
}
