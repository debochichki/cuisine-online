package com.softuni.cuisineonline.service.services.domain.impl;

import com.softuni.cuisineonline.data.models.Appliance;
import com.softuni.cuisineonline.data.models.Image;
import com.softuni.cuisineonline.data.models.Recipe;
import com.softuni.cuisineonline.data.repositories.ApplianceRepository;
import com.softuni.cuisineonline.data.repositories.RecipeRepository;
import com.softuni.cuisineonline.errors.MissingEntityException;
import com.softuni.cuisineonline.service.models.appliance.ApplianceCreateServiceModel;
import com.softuni.cuisineonline.service.models.appliance.ApplianceServiceModel;
import com.softuni.cuisineonline.service.services.domain.ApplianceService;
import com.softuni.cuisineonline.service.services.domain.CloudinaryService;
import com.softuni.cuisineonline.service.services.util.MappingService;
import com.softuni.cuisineonline.service.services.validation.ApplianceValidationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;


@Service
public class ApplianceServiceImpl implements ApplianceService {

    private final CloudinaryService cloudinaryService;
    private final ApplianceRepository applianceRepository;
    private final RecipeRepository recipeRepository;
    private final MappingService mappingService;
    private final ApplianceValidationService validationService;

    public ApplianceServiceImpl(
            CloudinaryService cloudinaryService,
            ApplianceRepository applianceRepository,
            RecipeRepository recipeRepository,
            MappingService mappingService,
            ApplianceValidationService validationService) {
        this.cloudinaryService = cloudinaryService;
        this.applianceRepository = applianceRepository;
        this.recipeRepository = recipeRepository;
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
        Appliance appliance = getApplianceById(id);
        return mappingService.map(appliance, ApplianceServiceModel.class);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        final Appliance appliance = getApplianceById(id);
        Image image = appliance.getImage();
        cloudinaryService.deleteImage(image.getPublicId());

        // Remove references to the appliance from all containing recipes
        final List<Recipe> modified = appliance.getRecipes().stream()
                .peek(r -> r.getAppliances().remove(appliance))
                .collect(toList());
        recipeRepository.saveAll(modified);
        applianceRepository.delete(appliance);
    }

    private Appliance getApplianceById(String id) {
        return applianceRepository.findById(id).orElseThrow(() ->
                new MissingEntityException("No appliance found in the database."));
    }
}
