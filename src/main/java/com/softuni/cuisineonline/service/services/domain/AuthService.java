package com.softuni.cuisineonline.service.services.domain;

import com.softuni.cuisineonline.service.models.UserLoginServiceModel;
import com.softuni.cuisineonline.service.models.UserRegisterServiceModel;

public interface AuthService {

    void register(UserRegisterServiceModel registerModel);

    String login(UserLoginServiceModel loginModel);
}
