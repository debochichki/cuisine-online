package com.softuni.cuisineonline.service.services.domain;

import com.softuni.cuisineonline.service.base.TestServiceBase;
import com.softuni.cuisineonline.service.models.ingredient.IngredientCreateServiceModel;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.InputMismatchException;
import java.util.List;

class IngredientServiceIT extends TestServiceBase {

    public static final String[][] VALID_INGREDIENTS_DATA =
            {{"olives", "100", "gr"}, {"tomatoes", "2", "pcs"}, {"ham", "200", "gr"}};

    @Autowired
    IngredientService service;

    @Test
    void mapToCreateModels_WhenCorrectDataIsPassed_ShouldMapAll() {
        List<IngredientCreateServiceModel> expectedIngredient =
                List.of(new IngredientCreateServiceModel("olives", "100", "gr"),
                        new IngredientCreateServiceModel("tomatoes", "2", "pcs"),
                        new IngredientCreateServiceModel("ham", "200", "gr"));

        List<IngredientCreateServiceModel> actualIngredients =
                service.mapToCreateModels(VALID_INGREDIENTS_DATA);

        Assert.assertEquals(expectedIngredient, actualIngredients);
    }

    @Test
    void mapToCreateModels_WhenIncorrectFormatData_ShouldThrow() {
        final String[][] incorrectData = {{"potatoes/1/kg"}};

        Assert.assertThrows(InputMismatchException.class, () -> service.mapToCreateModels(incorrectData));
    }
}