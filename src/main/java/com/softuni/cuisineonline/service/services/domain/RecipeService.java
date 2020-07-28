package com.softuni.cuisineonline.service.services.domain;

import com.softuni.cuisineonline.service.models.recipe.RecipeServiceModel;
import com.softuni.cuisineonline.service.models.recipe.RecipeUploadServiceModel;

import java.util.List;

public interface RecipeService {

    List<RecipeServiceModel> getAll();

    List<RecipeServiceModel> getAllByFilterOption(String filterOption);

    List<String> getRecipeTypesAsString();

    List<String> getRecipeFilterOptions();

    void upload(RecipeUploadServiceModel uploadModel);
}
