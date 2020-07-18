package com.softuni.cuisineonline.service.services.validation;

import com.softuni.cuisineonline.service.models.UserRegisterServiceModel;

public interface AuthValidationService {

    void validateRegisterModel(UserRegisterServiceModel registerModel);

}
