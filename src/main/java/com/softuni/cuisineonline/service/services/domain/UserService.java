package com.softuni.cuisineonline.service.services.domain;

import com.softuni.cuisineonline.data.models.Profile;
import com.softuni.cuisineonline.service.models.user.UserServiceModel;

public interface UserService {

    Profile getUserProfile(String username);

    UserServiceModel getUserDetails(String username);
}
