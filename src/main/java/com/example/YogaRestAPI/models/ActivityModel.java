package com.example.YogaRestAPI.models;

import com.example.YogaRestAPI.assemblers.ActivityTypeModelAssembler;
import com.example.YogaRestAPI.assemblers.LoungeLightModelAssembler;
import com.example.YogaRestAPI.assemblers.UserModelAssembler;
import com.example.YogaRestAPI.domain.Activity;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Relation(value = "activity", collectionRelation = "activities")
public class ActivityModel extends RepresentationModel<ActivityModel> {

    private static final UserModelAssembler usrModelAssembler = new UserModelAssembler();
    private static final ActivityTypeModelAssembler activityTypeModelAssembler = new ActivityTypeModelAssembler();
    private static final LoungeLightModelAssembler lngAssembler = new LoungeLightModelAssembler();

    private final String name;

    private final Integer capacity;

    private final LocalTime startTime;

    private final LocalTime finishTime;

    private final LocalDate dateOfActivity;

    private final BigDecimal price;

    private final Boolean isAvailable;

    private final UserModel mentor;

    private final ActivityTypeModel activityType;

    private final LoungeLightModel lounge;

    private final Iterable<UserModel> users;

    public ActivityModel(Activity activity) {
        this.name = activity.getName();
        this.capacity = activity.getCapacity();
        this.startTime = activity.getStartTime();
        this.finishTime = activity.getFinishTime();
        this.dateOfActivity = activity.getDateOfActivity();
        this.price = activity.getPrice();
        this.isAvailable = activity.getIsAvailable();
        this.mentor = usrModelAssembler.toModel(activity.getMentor());
        this.activityType = activityTypeModelAssembler.toModel(activity.getActivityType());

        //Чтобы не было циклической зависимости в JSON из за двусторонней связи hibernate
        //Проигнорировать это свойство нельзя - поэтому скрываем список мероприятий в помещении
        //Lounge lounge = activity.getLounge();
        //lounge.setActivities(Collections.emptyList());
        this.lounge = lngAssembler.toModel(activity.getLounge());

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

    public LoungeLightModel getLounge() {
        return lounge;
    }
    public Iterable<UserModel> getUsers() {
        return users;
    }
}
