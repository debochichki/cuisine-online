package com.softuni.cuisineonline.web.api.models.recipe;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RecipeResponseModel {

    private String id;

    private String typeIconUrl;

    private String title;

    private String uploaderUsername;

    private boolean canModify;
}
