package com.softuni.cuisineonline.service.services.domain;

import com.softuni.cuisineonline.service.models.auth.UserRegisterServiceModel;

public interface AuthService {

    void register(UserRegisterServiceModel registerModel);
}
