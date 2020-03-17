package com.example.YogaRestAPI.controllers;

import com.example.YogaRestAPI.domain.User;
import com.example.YogaRestAPI.errors.User.UserExistsException;
import com.example.YogaRestAPI.models.UserModel;
import com.example.YogaRestAPI.service.AccountService;
import com.example.YogaRestAPI.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/registration", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class RegistrationController {

    private final UserService userService;
    private final AccountService accountService;

    @Autowired
    public RegistrationController(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @ApiOperation("Зарегистрировать пользователя")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserModel> registration(@RequestBody User user) {
        accountService.passwordConfirm(user.getAccount());
        if (userService.checkUserExist(user)) {
            throw new UserExistsException(user.getEmail());
        }
        UserModel userModel = new UserModel(accountService.registration(user));
        userModel.add(linkTo(methodOn(UserController.class).findById(user.getId())).withRel("user"));
        return new ResponseEntity<>(userModel,HttpStatus.CREATED);
    }
}
