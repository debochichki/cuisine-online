package com.softuni.cuisineonline.web.view.models.recipe;

import com.softuni.cuisineonline.web.view.models.appliance.ApplianceViewModel;
import com.softuni.cuisineonline.web.view.models.ingredient.IngredientViewModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RecipeViewModel {

    private String id;

    private String title;

    private String imageUrl;

    private String type;

    private Integer duration;

    private Byte portions;

    private List<IngredientViewModel> ingredients;

    private List<ApplianceViewModel> appliances;

    private String description;

    private String uploaderUsername;
}
