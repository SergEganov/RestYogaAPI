package com.example.YogaRestAPI.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Data
@Entity
@Table(name = "activity_types")
public class ActivityType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "activity_type_id")
    private Long id;

    @Pattern(message = "Enter activity name in format: Yoga", regexp = "^[A-ZА-Я][a-zа-я]*")
    @NotBlank(message = "Name cant be empty!")
    @Length(min = 2, max = 30, message = "Activity name length from 2 to 30 characters")
    @Column(name = "activity_type_name", unique = true, nullable = false)
    private String name;

    @Column(name = "available")
    private Boolean available;
}
