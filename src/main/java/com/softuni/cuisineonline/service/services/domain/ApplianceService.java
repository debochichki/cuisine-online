package com.softuni.cuisineonline.service.services.domain;

import com.softuni.cuisineonline.service.models.appliance.ApplianceCreateServiceModel;
import com.softuni.cuisineonline.service.models.appliance.ApplianceServiceModel;

import java.util.List;

public interface ApplianceService {

    void create(ApplianceCreateServiceModel createModel);

    List<ApplianceServiceModel> getAll();

    ApplianceServiceModel getById(String id);

    void delete(String id);
}
