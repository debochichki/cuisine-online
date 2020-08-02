package com.softuni.cuisineonline.service.models.ingredient;

import com.softuni.cuisineonline.service.models.base.BaseServiceModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IngredientServiceModel extends BaseServiceModel {

    private String name;

    private String quantity;

    private String unitOfMeasurement;
}
