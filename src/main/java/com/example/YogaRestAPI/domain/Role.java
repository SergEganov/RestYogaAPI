package com.example.YogaRestAPI.domain;


import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_USER, ROLE_ADMIN, ROLE_MENTOR;

    @Override
    public String getAuthority() {
        return name();
    }
}
