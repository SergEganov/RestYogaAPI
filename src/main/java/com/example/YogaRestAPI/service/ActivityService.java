package com.example.YogaRestAPI.service;

import com.example.YogaRestAPI.domain.Activity;
import com.example.YogaRestAPI.domain.Lounge;
import com.example.YogaRestAPI.domain.User;
import com.example.YogaRestAPI.errors.User.UserAlreadySigned;
import com.example.YogaRestAPI.repos.ActivityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ActivityService {

    private final ActivityRepo activityRepo;

    private final UserService userService;

    @Autowired
    public ActivityService(ActivityRepo activityRepo, UserService userService) {
        this.activityRepo = activityRepo;
        this.userService = userService;
    }

    public Optional<Activity> findById(Long id) {
        return activityRepo.findById(id);
    }

    public List<Activity> findAll() {
        return activityRepo.findAll();
    }

    public Page<Activity> findAllPaginated(Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return activityRepo.findAll(pageable);
    }

    public Activity save(Activity activity) {
        return activityRepo.save(activity);
    }

    public Activity patch(Activity patch, Activity target) {
        if (patch.getName() != null) {
            target.setName(patch.getName());
        }
        if (patch.getStartTime() != null) {
            target.setStartTime(patch.getStartTime());
        }
        if (patch.getPrice() != null) {
            target.setPrice(patch.getPrice());
        }
        if (patch.getIsAvailable() != null) {
            target.setIsAvailable(patch.getIsAvailable());
        }
        if (patch.getFinishTime() != null) {
            target.setFinishTime(patch.getFinishTime());
        }
        if (patch.getCapacity() != null) {
            target.setCapacity(patch.getCapacity());
        }
        if (patch.getLounge() != null) {
            target.setLounge(patch.getLounge());
        }
        if (patch.getMentor() != null) {
            target.setMentor(patch.getMentor());
        }
        if (patch.getUsers() != null) {
            target.setUsers(patch.getUsers());
        }
        if (patch.getDateOfActivity() != null) {
            target.setDateOfActivity(patch.getDateOfActivity());
        }
        if (patch.getActivityType() != null) {
            target.setActivityType(patch.getActivityType());
        }
        return target;
    }

    public void deleteById(Long id) {
        activityRepo.deleteById(id);
    }

    public List<Activity> findByLoungeAndDateOfActivityOrderByStartTimeAsc(Lounge lounge, LocalDate dateOfActivity){
        return activityRepo.findByLoungeAndDateOfActivityOrderByStartTimeAsc(lounge, dateOfActivity);
    }

    public void checkForSignUp(Activity activity, User user) {
        User userFromDb = userService.findByEmail(user.getEmail());
        if (activity.getUsers().contains(userFromDb)) {
            throw new UserAlreadySigned(user.getEmail());
        }
        LocalDateTime todayDateTime = LocalDateTime.now();
        LocalDateTime activityDateTime = activity.getStartDateTime();
        if (todayDateTime.plusMinutes(30).isAfter(activityDateTime) ) {
            throw new RuntimeException("Registration for the event is finished!");
        }
        if (activity.getUsers().size() >= activity.getCapacity()) {
           throw new RuntimeException("Have no place, sorry.");
        }
    }

    public void signUpToActivity(Activity activity, User user) {
        User userFromDb =  userService.findByEmail(user.getEmail());
        if(userFromDb == null) {
            userService.save(user);
            activity.getUsers().add(userService.findByEmail(user.getEmail()));
        } else {
            activity.getUsers().add(userFromDb);
        }
       save(activity);
    }

    public void signOutFromActivity(Activity activity, Long userId) {
        Optional<User> user = userService.findById(userId);
        if (user.isPresent()) {
            activity.getUsers().remove(user.get());
            save(activity);
        }
    }

    public boolean checkLegalActivityTime(Activity activity, BindingResult bindingResult){
        if(!checkStartAndFinishTime(activity,bindingResult)) {
            return false;
        }
        if(!checkLoungeCapacity(activity, bindingResult)) {
            return false;
        }
        if (!checkLoungeWorkPeriod(activity,bindingResult)) {
            return false;
        }

        List<Activity> activities = findByLoungeAndDateOfActivityOrderByStartTimeAsc(activity.getLounge(), activity.getDateOfActivity());
        if (activity.getId() != null) {
            //если идет операция checkForUpdate - выталкиваем текущую дату из списка проверок
            Activity activityFromDb = findById(activity.getId()).get();
            activities.remove(activityFromDb);
        }
        if (activities.isEmpty()) {
            return true;
        }
        for(Activity act: activities) {
            if (!((activity.getStartTime().isBefore(act.getStartTime())
                    && activity.getFinishTime().plusMinutes(29).isBefore(act.getStartTime()))
                    || activity.getStartTime().isAfter(act.getFinishTime().plusMinutes(29)))
            ) {
                bindingResult.addError(new FieldError(
                        "activity",
                        "startTime",
                        "Check activity time - 30 minutes between activities: "
                                + activity.getStartTime()
                                + " " + activity.getFinishTime()
                                + ". We have the same time activity in schedule " + act.getName()
                                + " " + act.getStartTime() + " " + act.getFinishTime()));
                return false;
            }
        }
        return true;
    }

    private boolean checkLoungeWorkPeriod(Activity activity, BindingResult bindingResult) {
        if (activity.getStartTime().isBefore(activity.getLounge().getStartTime()) ||
                activity.getStartTime().isAfter(activity.getLounge().getFinishTime())
        ) {
            bindingResult.addError(new FieldError(
                    "activity",
                    "startTime",
                    "Check the time of the event: from " + activity.getStartTime()
                            + " to " + activity.getFinishTime()
                            + ". The lounge is working from " + activity.getLounge().getStartTime() + " to "
                            + activity.getLounge().getFinishTime()));
            return false;
        }
        if (activity.getFinishTime().isBefore(activity.getLounge().getStartTime()) ||
                activity.getFinishTime().isAfter(activity.getLounge().getFinishTime())
        ) {
            bindingResult.addError(new FieldError(
                    "activity",
                    "finishTime",
                    "Check the time of the event: from " + activity.getStartTime()
                            + " to " + activity.getFinishTime()
                            + ". The lounge is working from " + activity.getLounge().getStartTime() + " to "
                            + activity.getLounge().getFinishTime()));
            return false;
        }
        return true;
    }

    private boolean checkLoungeCapacity(Activity activity, BindingResult bindingResult) {
        if (activity.getCapacity() > activity.getLounge().getCapacity()) {
            bindingResult.addError(new FieldError(
                    "activity",
                    "capacity",
                    "Check the capacity of the event: " + activity.getCapacity()
                            + ". The lounge max capacity is " + activity.getLounge().getCapacity()));
            return false;
        }
        return true;
    }

    private boolean checkStartAndFinishTime(Activity activity, BindingResult bindingResult){
        if (activity.getStartTime().isAfter(activity.getFinishTime())) {
            bindingResult.addError(new FieldError(
                    "activity",
                    "finishTime",
                    "Check the time of the event: from " + activity.getStartTime()
                            + " to " + activity.getFinishTime()
                            + ". the finish can't be before the start"));
            return false;
        }
        return true;
    }
}

