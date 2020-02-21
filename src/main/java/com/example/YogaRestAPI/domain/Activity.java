package com.example.YogaRestAPI.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Data
@Entity
@Table(name = "activities")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "activity_id")
    private Long id;

    @Pattern(message = "Enter activity name in format: Aengara",
            regexp = "^[A-ZА-Я][a-zа-я]*")
    @NotBlank(message = "Name can't be empty!")
    @Length(min = 2, max = 25, message = "Name length from 2 to 25 characters")
    @Column(name = "activity_name", nullable = false)
    private String name;

    @Min(value = 1, message = "Capacity must be > 0")
    @NotNull(message = "Activity capacity can't be empty!")
    @Digits(integer=3, fraction=0, message = "Max capacity is 999")
    @Column(name = "activity_capacity", nullable = false)
    private Integer capacity;

    @NotNull(message = "Not empty")
    @DateTimeFormat(pattern = "HH:mm")
    @Column(name = "activity_start", nullable = false)
    private LocalTime startTime;

    @NotNull(message = "Not empty")
    @DateTimeFormat(pattern = "HH:mm")
    @Column(name = "activity_finish", nullable = false)
    private LocalTime finishTime;

    @NotNull(message = "Not empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future(message = "Please enter correct date(can't be earlier than today)")
    @Column(name = "date")
    private LocalDate dateOfActivity;

    @NotNull(message = "Not empty")
    @Min(value = 1, message = "Price must be > 0")
    @Column(name = "activity_price", nullable = false)
    private BigDecimal price;

    @Column(name = "available")
    private Boolean isAvailable;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id")
    private User mentor;

    @NotNull(message = "Choose activity type. If u need a new type - u can create it, than create an activity!")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_type_id")
    private ActivityType activityType;

    @NotNull(message = "Choose lounge. It cant be empty")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lounge_id")
    private Lounge lounge;

    @ManyToMany
    @JoinTable(
            name = "activity_users",
            joinColumns = {@JoinColumn(name = "activity_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<User> users = new HashSet<>();

    public LocalDateTime getStartDateTime(){
        return LocalDateTime.of(dateOfActivity, startTime);
    }
}