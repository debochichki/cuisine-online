package com.softuni.cuisineonline.service.services.domain.impl;

import com.softuni.cuisineonline.data.models.Appliance;
import com.softuni.cuisineonline.data.models.Image;
import com.softuni.cuisineonline.data.repositories.ApplianceRepository;
import com.softuni.cuisineonline.errors.MissingEntityException;
import com.softuni.cuisineonline.service.models.appliance.ApplianceCreateServiceModel;
import com.softuni.cuisineonline.service.models.appliance.ApplianceServiceModel;
import com.softuni.cuisineonline.service.services.domain.ApplianceService;
import com.softuni.cuisineonline.service.services.domain.CloudinaryService;
import com.softuni.cuisineonline.service.services.util.MappingService;
import com.softuni.cuisineonline.service.services.validation.ApplianceValidationService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class ApplianceServiceImpl implements ApplianceService {

    private final CloudinaryService cloudinaryService;
    private final ApplianceRepository applianceRepository;
    private final MappingService mappingService;
    private final ApplianceValidationService validationService;

    public ApplianceServiceImpl(
            CloudinaryService cloudinaryService,
            ApplianceRepository applianceRepository,
            MappingService mappingService,
            ApplianceValidationService validationService) {
        this.cloudinaryService = cloudinaryService;
        this.applianceRepository = applianceRepository;
        this.mappingService = mappingService;
        this.validationService = validationService;
    }

    @Override
    public void create(ApplianceCreateServiceModel createModel) {
        validationService.validateCreateModel(createModel);

        String name = createModel.getName();
        Image image = cloudinaryService.uploadImage(createModel.getImage());
        Appliance appliance = new Appliance();
        appliance.setName(name);
        appliance.setImage(image);
        applianceRepository.save(appliance);
    }

    @Override
    public List<ApplianceServiceModel> getAll() {
        return mappingService.mapAll(applianceRepository.findAll(), ApplianceServiceModel.class);
    }

    @Override
    public ApplianceServiceModel getById(String id) {
        Appliance appliance = applianceRepository.findById(id).orElseThrow(() ->
                new MissingEntityException("No appliance found in the database."));
        return mappingService.map(appliance, ApplianceServiceModel.class);
    }

    @Override
    public void delete(String id) {
        Appliance appliance = applianceRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("No appliance found in the database."));
        String publicId = appliance.getImage().getPublicId();
        cloudinaryService.deleteImage(publicId);
        applianceRepository.delete(appliance);
    }
}
