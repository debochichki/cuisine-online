package com.softuni.cuisineonline.service.services.domain.impl;

import com.softuni.cuisineonline.service.models.ingredient.IngredientCreateServiceModel;
import com.softuni.cuisineonline.service.services.domain.IngredientService;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

@Service
public class IngredientServiceImpl implements IngredientService {

    @Override
    public List<IngredientCreateServiceModel> mapToCreateModels(String[][] ingredientsData) {
        Field[] declaredFields = IngredientCreateServiceModel.class.getDeclaredFields();
        List<IngredientCreateServiceModel> ingredients = new ArrayList<>();
        for (String[] ingredientData : ingredientsData) {
            if (ingredientData.length != declaredFields.length) {
                throw new InputMismatchException("The input does not match the required format.");
            }
            String name = ingredientData[0];
            String quantity = ingredientData[1];
            String unit = ingredientData[2];

            ingredients.add(new IngredientCreateServiceModel(name, quantity, unit));
        }

        return ingredients;
    }
}
