package com.softuni.cuisineonline.service.models.recipe;

import com.softuni.cuisineonline.service.models.base.BaseServiceModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RecipeServiceModel extends BaseServiceModel {

    private String typeIconUrl;

    private String title;

    private String uploaderUsername;

    private boolean canModify;
}

