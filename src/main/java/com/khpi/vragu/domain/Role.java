package com.khpi.vragu.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER, ADMIN, SASHOK;


    @Override
    public String getAuthority() {
        return name();
    }
}
