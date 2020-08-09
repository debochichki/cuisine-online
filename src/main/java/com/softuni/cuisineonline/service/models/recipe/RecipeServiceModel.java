package com.softuni.cuisineonline.service.models.recipe;

import com.softuni.cuisineonline.service.models.appliance.ApplianceServiceModel;
import com.softuni.cuisineonline.service.models.base.BaseServiceModel;
import com.softuni.cuisineonline.service.models.ingredient.IngredientServiceModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class RecipeServiceModel extends BaseServiceModel {

    private String title;

    private String imageUrl;

    private String type;

    private Integer duration;

    private Byte portions;

    private List<IngredientServiceModel> ingredients;

    private List<ApplianceServiceModel> appliances;

    private String description;

    private String uploaderUsername;
}

