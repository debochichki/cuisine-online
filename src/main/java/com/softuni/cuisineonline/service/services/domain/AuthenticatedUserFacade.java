package com.softuni.cuisineonline.service.services.domain;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public interface AuthenticatedUserFacade {

    String getPrincipalName();

    List<? extends GrantedAuthority> getUserAuthorities();
}
