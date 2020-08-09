package com.softuni.cuisineonline.service.models.ingredient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientCreateServiceModel {

    private String name;

    private String quantity;

    private String unitOfMeasurement;
}
