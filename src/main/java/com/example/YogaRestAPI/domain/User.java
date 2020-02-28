package com.example.YogaRestAPI.domain;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "usrs")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

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

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    public User() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                email.equals(user.email) &&
                Objects.equals(phoneNumber, user.phoneNumber) &&
                Objects.equals(birth, user.birth) &&
                Objects.equals(account, user.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, phoneNumber, birth, account);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
