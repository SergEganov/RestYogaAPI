package com.example.YogaRestAPI.domain;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Objects;

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

    public ActivityType() {
    }

    public ActivityType(String name) {
        this.name = name;
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

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ActivityType that = (ActivityType) o;
        return id.equals(that.id) &&
                name.equals(that.name) &&
                Objects.equals(available, that.available);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, available);
    }
}
