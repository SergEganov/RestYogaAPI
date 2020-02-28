package com.example.YogaRestAPI.controllers;

import com.example.YogaRestAPI.assemblers.UserModelAssembler;
import com.example.YogaRestAPI.domain.User;
import com.example.YogaRestAPI.errors.User.UserNotFoundException;
import com.example.YogaRestAPI.models.UserModel;
import com.example.YogaRestAPI.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public CollectionModel<UserModel> findAll(@RequestParam(value = "pageN", defaultValue = "0") Integer page,
                                              @RequestParam(value = "size", defaultValue = "5") Integer size) {
        Page<User> usersPaged = userService.findAllPaginated(page, size);
        CollectionModel<UserModel> users = new UserModelAssembler().toCollectionModel(usersPaged);
        users.add(linkTo(methodOn(UserController.class).findAll(page, size)).withRel("users"));
        return users;
    }

    @GetMapping("/{id}")
    public UserModel findById(@PathVariable("id") Long id) {
        User user = userService.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        UserModel userModel = new UserModel(user);
        userModel.add(linkTo(methodOn(UserController.class).findById(id)).withRel("user"));
        return userModel;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserModel createNew(@RequestBody User user) {
        UserModel userModel = new UserModel(userService.save(user));
        userModel.add(linkTo(methodOn(UserController.class).findById(user.getId())).withRel("user"));
        return userModel;
    }

    @PutMapping("/{id}")
    public UserModel saveOrUpdate(@RequestBody User user, @PathVariable Long id) {
        return userService.findById(id)
                .map(usr -> {
                    BeanUtils.copyProperties(user, usr);
                    usr.setId(id);
                    userService.save(usr);
                    UserModel userModel = new UserModel(usr);
                    userModel.add(linkTo(methodOn(UserController.class)
                            .saveOrUpdate(user, id))
                            .withRel("user"));
                    return userModel;
                })
                .orElseGet(() -> {
                    User newUser = userService.save(user);
                    UserModel userModel = new UserModel(newUser);
                    userModel.add(linkTo(methodOn(UserController.class)
                            .saveOrUpdate(user, newUser.getId()))
                            .withRel("user"));
                    return userModel;
                });
    }

    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserModel patch(@RequestBody User patch, @PathVariable Long id) {
        User user = userService.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        UserModel userModel = new UserModel(userService.save(userService.patch(patch, user)));
        userModel.add(linkTo(methodOn(UserController.class).patch(patch, id)).withRel("user"));
        return userModel;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}