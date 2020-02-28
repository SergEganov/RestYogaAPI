package com.example.YogaRestAPI.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "lounge")
public class Lounge{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "lounge_id")
    private Long id;

    @NotBlank(message = "First name can't be empty!")
    @Column(name = "lounge_name", unique = true, nullable = false)
    private String name;

    @NotBlank(message = "First name can't be empty!")
    @Column(name = "lounge_address", nullable = false)
    private String address;

    @Min(value = 1, message = "Capacity must be > 0")
    @NotNull(message = "Lounge capacity can't be empty!")
    @Digits(integer=3, fraction=0, message = "Max capacity is 999")
    @Column(name = "lounge_capacity", nullable = false)
    private int capacity;

    @NotNull(message = "Not empty")
    @DateTimeFormat(pattern = "HH:mm")
    @Column(name = "lounge_work_start", nullable = false)
    private LocalTime startTime;

    @NotNull(message = "Not empty")
    @DateTimeFormat(pattern = "HH:mm")
    @Column(name = "lounge_work_finish", nullable = false)
    private LocalTime finishTime;

    @OneToMany(mappedBy = "lounge", cascade = CascadeType.ALL)
    private List<Activity> activities;

    @ManyToMany
    @JoinTable(
            name = "lounge_activity_types",
            joinColumns = { @JoinColumn(name = "lounge_id") },
            inverseJoinColumns = { @JoinColumn(name = "activity_type_id") }
    )
    private Set<ActivityType> activityTypes = new HashSet<>();

    public Lounge() {
    }

    public Lounge(String name, String address, int capacity, LocalTime startTime,
                  LocalTime finishTime, List<Activity> activities, Set<ActivityType> activityTypes) {
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.activities = activities;
        this.activityTypes = activityTypes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(LocalTime finishTime) {
        this.finishTime = finishTime;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public Set<ActivityType> getActivityTypes() {
        return activityTypes;
    }

    public void setActivityTypes(Set<ActivityType> activityTypes) {
        this.activityTypes = activityTypes;
    }
}
