package com.softuni.cuisineonline.service.services.validation.impl;

import com.softuni.cuisineonline.data.models.Recipe;
import com.softuni.cuisineonline.data.repositories.UserRepository;
import com.softuni.cuisineonline.errors.ServerException;
import com.softuni.cuisineonline.errors.ValidationException;
import com.softuni.cuisineonline.service.models.recipe.RecipeEditServiceModel;
import com.softuni.cuisineonline.service.models.recipe.RecipeUploadServiceModel;
import com.softuni.cuisineonline.service.services.validation.RecipeValidationService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

import static com.softuni.cuisineonline.service.services.util.Constants.*;
import static com.softuni.cuisineonline.service.services.util.InputUtil.areAllFilled;
import static com.softuni.cuisineonline.service.services.util.InputUtil.isLengthInBounds;

@Service
public class RecipeValidationServiceImpl implements RecipeValidationService {

    private final UserRepository userRepository;

    public RecipeValidationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void validateUploadModel(RecipeUploadServiceModel uploadModel) {
        MultipartFile image = uploadModel.getImage();
        if (image == null || image.isEmpty()) {
            throw new ValidationException("You must specify an image file.");
        }

        String uploaderUsername = uploadModel.getUploaderUsername();
        if (uploaderUsername == null || uploaderUsername.isBlank()) {
            throw new ServerException("Video upload is initiated by non-logged user.");
        }

        if (!userRepository.existsByUsername(uploaderUsername)) {
            throw new ServerException(
                    "No user with username: " + uploaderUsername + " exists in the database.");
        }

        Recipe.Type type = uploadModel.getType();
        String title = uploadModel.getTitle();
        String description = uploadModel.getDescription();
        String ingredientsData = uploadModel.getIngredientsData();
        Integer duration = uploadModel.getDuration();
        Byte portions = uploadModel.getPortions();

        validateParameters(type, title, description, ingredientsData, duration, portions);
    }

    @Override
    public void validateEditModel(RecipeEditServiceModel editModel) {
        Recipe.Type type = editModel.getType();
        String title = editModel.getTitle();
        String description = editModel.getDescription();
        String ingredientsData = editModel.getIngredientsData();
        Integer duration = editModel.getDuration();
        Byte portions = editModel.getPortions();

        validateParameters(type, title, description, ingredientsData, duration, portions);
    }

    private void validateParameters(Recipe.Type type, String title, String description, String ingredientsData, Integer duration, Byte portions) {
        if (type == null) {
            throw new ValidationException("You must specify an recipe type.");
        }

        boolean allFilled = areAllFilled(title, description, ingredientsData);
        if (!allFilled) {
            throw new ValidationException("Fill all obligatory fields.");
        }

        if (!isLengthInBounds(title, TITLE_LENGTH_LOWER_BOUND, TITLE_LENGTH_UPPER_BOUND)) {
            throw new ValidationException(
                    String.format("Recipe title length needs to be between %d and %d symbols",
                            TITLE_LENGTH_LOWER_BOUND,
                            TITLE_LENGTH_UPPER_BOUND));
        }

        if (!isLengthInBounds(description,
                RECIPE_DESCRIPTION_LENGTH_LOWER_BOUND,
                RECIPE_DESCRIPTION_LENGTH_UPPER_BOUND)) {
            throw new ValidationException(
                    String.format("Recipe description length needs to be between %d and %d symbols",
                            RECIPE_DESCRIPTION_LENGTH_LOWER_BOUND,
                            RECIPE_DESCRIPTION_LENGTH_UPPER_BOUND));
        }

        if (!areValidIntegers(duration, portions)) {
            throw new ValidationException("Recipe duration and portions must be positive integers.");
        }
    }

    private boolean areValidIntegers(Number... numbers) {
        return Arrays.stream(numbers)
                .allMatch(n -> n != null
                        && isAssignableFrom(n, Byte.class, Short.class, Integer.class, Long.class)
                        && n.longValue() > 0L);
    }

    private boolean isAssignableFrom(Object src, Class... classes) {
        Class<?> sourceClass = src.getClass();
        return Arrays.stream(classes).anyMatch(sourceClass::isAssignableFrom);
    }
}
