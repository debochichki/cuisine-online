package com.softuni.cuisineonline.service.models.ingredient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientCreateServiceModel {

    private String name;

    private String quantity;

    private String unitOfMeasurement;
}
