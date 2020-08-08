package com.softuni.cuisineonline.service.services.domain;

import com.softuni.cuisineonline.service.models.recipe.RecipeBaseServiceModel;
import com.softuni.cuisineonline.service.models.recipe.RecipeEditServiceModel;
import com.softuni.cuisineonline.service.models.recipe.RecipeServiceModel;
import com.softuni.cuisineonline.service.models.recipe.RecipeUploadServiceModel;

import java.util.List;

public interface RecipeService {

    RecipeServiceModel getById(String id);

    String getUploaderUsername(String recipeId);

    List<RecipeBaseServiceModel> getAllByFilterOption(String filterOption);

    List<String> getRecipeTypesAsStringValues();

    List<String> getRecipeFilterOptions();

    void upload(RecipeUploadServiceModel uploadModel);

    void edit(RecipeEditServiceModel editModel);

    void deleteById(String id);
}
