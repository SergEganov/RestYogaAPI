package com.example.YogaRestAPI.domain;

import lombok.Data;
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
@Data
public class Lounge extends BaseEntity{

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

}
