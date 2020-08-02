package com.softuni.cuisineonline.service.services.validation;

import com.softuni.cuisineonline.service.models.recipe.RecipeEditServiceModel;
import com.softuni.cuisineonline.service.models.recipe.RecipeUploadServiceModel;

public interface RecipeValidationService {

    void validateUploadModel(RecipeUploadServiceModel uploadModel);

    void validateEditModel(RecipeEditServiceModel editModel);
}
