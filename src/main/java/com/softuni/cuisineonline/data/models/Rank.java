package com.softuni.cuisineonline.data.models;

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
}
