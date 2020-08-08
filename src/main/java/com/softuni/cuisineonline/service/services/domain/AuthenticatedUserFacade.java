package com.softuni.cuisineonline.service.services.domain;

import java.util.List;

public interface AuthenticatedUserFacade {

    String getPrincipalName();

    List<String> getPrincipalAuthorities();
}
