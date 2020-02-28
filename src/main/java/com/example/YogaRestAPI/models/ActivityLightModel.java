package com.example.YogaRestAPI.models;

import com.example.YogaRestAPI.assemblers.ActivityTypeModelAssembler;
import com.example.YogaRestAPI.assemblers.UserModelAssembler;
import com.example.YogaRestAPI.domain.Activity;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Relation(value = "activity", collectionRelation = "activities")
public class ActivityLightModel extends RepresentationModel<ActivityLightModel> {


    private static final UserModelAssembler usrModelAssembler = new UserModelAssembler();
    private static final ActivityTypeModelAssembler activityTypeModelAssembler = new ActivityTypeModelAssembler();

    // Исключено помещение
    private final String name;

    private final Integer capacity;

    private final LocalTime startTime;

    private final LocalTime finishTime;

    private final LocalDate dateOfActivity;

    private final BigDecimal price;

    private final Boolean isAvailable;

    private final UserModel mentor;

    private final ActivityTypeModel activityType;

    private final Iterable<UserModel> users;

    public ActivityLightModel(Activity activity) {
        this.name = activity.getName();
        this.capacity = activity.getCapacity();
        this.startTime = activity.getStartTime();
        this.finishTime = activity.getFinishTime();
        this.dateOfActivity = activity.getDateOfActivity();
        this.price = activity.getPrice();
        this.isAvailable = activity.getIsAvailable();
        this.mentor = usrModelAssembler.toModel(activity.getMentor());
        this.activityType = activityTypeModelAssembler.toModel(activity.getActivityType());
        this.users = usrModelAssembler.toCollectionModel(activity.getUsers());
    }

    public String getName() {
        return name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getFinishTime() {
        return finishTime;
    }

    public LocalDate getDateOfActivity() {
        return dateOfActivity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public UserModel getMentor() {
        return mentor;
    }

    public ActivityTypeModel getActivityType() {
        return activityType;
    }

    public Iterable<UserModel> getUsers() {
        return users;
    }
}
