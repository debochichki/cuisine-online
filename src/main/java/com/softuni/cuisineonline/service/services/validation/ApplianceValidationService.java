package com.softuni.cuisineonline.service.services.validation;

import com.softuni.cuisineonline.service.models.appliance.ApplianceCreateServiceModel;

public interface ApplianceValidationService {

    void validateCreateModel(ApplianceCreateServiceModel createModel);
}
