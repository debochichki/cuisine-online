package com.softuni.cuisineonline.service.models.recipe;

import com.softuni.cuisineonline.service.models.appliance.ApplianceServiceModel;
import com.softuni.cuisineonline.service.models.base.BaseServiceModel;
import com.softuni.cuisineonline.service.models.ingredient.IngredientServiceModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
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
