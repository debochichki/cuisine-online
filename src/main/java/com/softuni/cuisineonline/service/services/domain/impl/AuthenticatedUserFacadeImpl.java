package com.softuni.cuisineonline.service.services.domain.impl;

import com.softuni.cuisineonline.errors.ServerException;
import com.softuni.cuisineonline.service.services.domain.AuthenticatedUserFacade;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthenticatedUserFacadeImpl implements AuthenticatedUserFacade {

    @Override
    public String getPrincipalName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new ServerException("User is not authenticated");
        }

        return authentication.getName();
    }

    @Override
    public List<? extends GrantedAuthority> getUserAuthorities() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ArrayList<>(authentication.getAuthorities());
    }
}
