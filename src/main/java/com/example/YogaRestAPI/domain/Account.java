package com.example.YogaRestAPI.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Entity
@Table(name = "accounts")
@Data
public class Account extends BaseEntity implements UserDetails {

    @NotBlank(message = "Login can't be empty! Use your email")
    @Email(message = "Check email form to: Test@test.com")
    @Column(name = "login", unique = true, nullable = false)
    private String login;

    @Size(min=5, message = "At least 5 chars")
    @Column(name = "password", nullable = false)
    private String password;

    @Transient
    private String passwordConfirm;

    @ManyToMany
    @JoinTable(
            name = "account_activities",
            joinColumns = {@JoinColumn(name = "account_id")},
            inverseJoinColumns = {@JoinColumn(name = "activity_id")}
    )
    private Set<Activity> listOfEvents = new HashSet<>();

    @OneToOne(mappedBy = "account")
    private User user;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "account_role", joinColumns = @JoinColumn(name = "account_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
