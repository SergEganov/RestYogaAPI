package com.example.YogaRestAPI.domain;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "activity_types")
@Data
public class ActivityType extends BaseEntity{

    @Pattern(message = "Enter activity name in format: Yoga", regexp = "^[A-ZА-Я][a-zа-я]*")
    @NotBlank(message = "Name cant be empty!")
    @Length(min = 2, max = 30, message = "Activity name length from 2 to 30 characters")
    @Column(name = "activity_type_name", unique = true, nullable = false)
    private String name;

    @Column(name = "available")
    private Boolean available;
}
