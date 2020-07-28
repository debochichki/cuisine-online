package com.softuni.cuisineonline.service.services.domain;

import com.softuni.cuisineonline.service.models.ingredient.IngredientCreateServiceModel;

import java.util.List;

public interface IngredientService {

    List<IngredientCreateServiceModel> mapToCreateModels(String[][] ingredientsData);
}
