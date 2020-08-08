package com.softuni.cuisineonline.data.models;

import com.softuni.cuisineonline.errors.ServerException;

import java.util.Arrays;
import java.util.Comparator;

public enum Rank {
    NOVICE(0),
    ENTHUSIAST(5),
    COOK(10),
    CHEF(15),
    MASTER_CHEF(25);

    private final int recipesCount;

    Rank(int recipesCount) {
        this.recipesCount = recipesCount;
    }

    public int getRecipesCount() {
        return recipesCount;
    }

    public static Rank resolve(final int uploadedRecipes) {
        if (uploadedRecipes < 0) {
            throw new IllegalArgumentException("Value must be non-negative.");
        }

        return Arrays.stream(Rank.values())
                .filter(r -> uploadedRecipes >= r.getRecipesCount())
                .max(Comparator.comparing(Rank::getRecipesCount))
                .orElseThrow(() ->
                        new ServerException(
                                "Could not resolve user rank with argument: "
                                        + uploadedRecipes));
    }
}
