package com.softuni.cuisineonline.service.services.domain.impl;

import com.softuni.cuisineonline.data.models.Appliance;
import com.softuni.cuisineonline.data.models.Image;
import com.softuni.cuisineonline.data.repositories.ApplianceRepository;
import com.softuni.cuisineonline.service.models.appliance.ApplianceCreateServiceModel;
import com.softuni.cuisineonline.service.services.domain.ApplianceService;
import com.softuni.cuisineonline.service.services.domain.CloudinaryService;
import org.springframework.stereotype.Service;

@Service
public class ApplianceServiceImpl implements ApplianceService {

    private final CloudinaryService cloudinaryService;
    private final ApplianceRepository applianceRepository;

    public ApplianceServiceImpl(
            CloudinaryService cloudinaryService,
            ApplianceRepository applianceRepository) {
        this.cloudinaryService = cloudinaryService;
        this.applianceRepository = applianceRepository;
    }

    @Override
    public void create(ApplianceCreateServiceModel createModel) {
        String name = createModel.getName();
        Image image = cloudinaryService.uploadImage(createModel.getImage());
        Appliance appliance = new Appliance();
        appliance.setName(name);
        appliance.setImage(image);
        applianceRepository.save(appliance);
    }
}
