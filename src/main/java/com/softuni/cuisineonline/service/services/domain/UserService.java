package com.softuni.cuisineonline.service.services.domain;

import com.softuni.cuisineonline.data.models.Profile;

public interface UserService {

    Profile getUserProfile(String username);
}
