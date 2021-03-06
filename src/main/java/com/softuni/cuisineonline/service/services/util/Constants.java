package com.softuni.cuisineonline.service.services.util;

public final class Constants {

    public static final String YOU_TUBE_DOMAIN_URL = "https://www.youtube.com/watch?v=";

    public static final int USERNAME_LENGTH_LOWER_BOUND = 3;
    public static final int USERNAME_LENGTH_UPPER_BOUND = 10;

    public static final int TITLE_LENGTH_LOWER_BOUND = 3;
    public static final int TITLE_LENGTH_UPPER_BOUND = 50;

    public static final int COMMENT_CONTENT_LENGTH_LOWER_BOUND = 1;
    public static final int COMMENT_CONTENT_LENGTH_UPPER_BOUND = 500;

    public static final int RECIPE_DESCRIPTION_LENGTH_LOWER_BOUND = 10;
    public static final int RECIPE_DESCRIPTION_LENGTH_UPPER_BOUND = 1000;

    public static final String DEFAULT_EMPTY_FIELD_VALUE = "NaN";

    private Constants() {
        // Prevent instantiation
    }
}
