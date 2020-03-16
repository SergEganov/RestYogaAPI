package com.example.YogaRestAPI.domain;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
public class User extends BaseEntity{

    @Pattern(message = "Enter your first name in format: Jack",
            regexp = "^[A-ZА-Я][a-zа-я]*")
    @NotBlank(message = "First name can't be empty!")
    @Length(min = 2, max = 25, message = "First name length from 2 to 25 characters")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Pattern(message = "Enter your last name in format: Jackson",
            regexp = "^[A-ZА-Я][a-zа-я]*")
    @NotBlank(message = "Last name can't be empty!")
    @Length(min = 2, max = 25, message = "Last name length from 2 to 25 characters")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotBlank(message = "Email can't be empty!")
    @Email(message = "Check email form to: Test@test.com")
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Phone number can't be empty!")
    @Digits(integer=11, fraction=0, message = "Check that you entered the numbers")
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Please enter correct birthday(date can't be later than today)")
    @Column(name = "birthday")
    private LocalDate birth;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

}
