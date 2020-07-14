package com.softuni.cuisineonline.data.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Recipe {

    public enum Type {
        SALAD, SOUP, MAIN_DISH, DESSERT, DRINK,
    }

    private String title;

    private Image image;

    private Type type;

    /**
     * The duration of the recipe preparation
     */
    private Integer duration;

    private Byte portions;

    private List<Ingredient> ingredients;

    private List<Appliance> appliances;

    private String description;

    private Profile uploader;
}
