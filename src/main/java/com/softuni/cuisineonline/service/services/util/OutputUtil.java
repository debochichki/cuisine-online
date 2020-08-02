package com.softuni.cuisineonline.service.services.util;

import com.softuni.cuisineonline.web.view.models.ingredient.IngredientViewModel;

import java.util.List;

import static java.util.stream.Collectors.joining;
import static org.thymeleaf.util.StringUtils.isEmptyOrWhitespace;

public final class OutputUtil {

    private OutputUtil() {
        // Prevent instantiation
    }

    public static String getIngredientListFormatted(List<IngredientViewModel> ingredients) {
        return ingredients.stream().map(i ->
                String.format("%s, %s, %s",
                        i.getName(),
                        i.getQuantity(),
                        i.getUnitOfMeasurement()))
                .collect(joining(System.lineSeparator()));
    }

    public static String ifEmptySetDefault(String value, String defaultString) {
        return isEmptyOrWhitespace(value) ? defaultString : value;
    }
}
