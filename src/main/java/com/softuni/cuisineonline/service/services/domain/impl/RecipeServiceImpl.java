package com.softuni.cuisineonline.service.services.domain.impl;

import com.softuni.cuisineonline.data.models.*;
import com.softuni.cuisineonline.data.repositories.ApplianceRepository;
import com.softuni.cuisineonline.data.repositories.IngredientRepository;
import com.softuni.cuisineonline.data.repositories.RecipeRepository;
import com.softuni.cuisineonline.service.models.ingredient.IngredientCreateServiceModel;
import com.softuni.cuisineonline.service.models.recipe.RecipeServiceModel;
import com.softuni.cuisineonline.service.models.recipe.RecipeUploadServiceModel;
import com.softuni.cuisineonline.service.services.domain.CloudinaryService;
import com.softuni.cuisineonline.service.services.domain.IngredientService;
import com.softuni.cuisineonline.service.services.domain.RecipeService;
import com.softuni.cuisineonline.service.services.domain.UserService;
import com.softuni.cuisineonline.service.services.util.MappingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.softuni.cuisineonline.service.services.util.InputUtil.parseData;
import static java.util.stream.Collectors.toList;

@Service
public class RecipeServiceImpl implements RecipeService {

    private static final String RECIPE_TYPE_ICON_URL_TEMPLATE = "/icon/%s_icon.png";
    private static final String INGREDIENT_TEXT_AREA_VALUE_SEPARATOR = ",";
    private static final String ALL_RECIPES_FILTER_OPTION = "ALL";
    private final RecipeRepository recipeRepository;
    private final ApplianceRepository applianceRepository;
    private final IngredientRepository ingredientRepository;
    private final UserService userService;
    private final IngredientService ingredientService;
    private final MappingService mappingService;
    private final CloudinaryService cloudinaryService;

    public RecipeServiceImpl(
            RecipeRepository recipeRepository,
            ApplianceRepository applianceRepository,
            IngredientRepository ingredientRepository,
            IngredientService ingredientService,
            UserService userService,
            MappingService mappingService,
            CloudinaryService cloudinaryService) {
        this.recipeRepository = recipeRepository;
        this.applianceRepository = applianceRepository;
        this.ingredientRepository = ingredientRepository;
        this.ingredientService = ingredientService;
        this.userService = userService;
        this.mappingService = mappingService;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public List<RecipeServiceModel> getAll() {
        return recipeRepository.findAllByOrderByTitleAsc().stream()
                .map(this::mapToServiceModel).collect(toList());
    }

    @Override
    public List<RecipeServiceModel> getAllByFilterOption(String filterOption) {
        List<Recipe> filteredRecipes = filterOption.equals(ALL_RECIPES_FILTER_OPTION)
                ? recipeRepository.findAll()
                : recipeRepository
                .findAllByTypeOrderByTitleAsc(Recipe.Type.valueOf(filterOption));

        return filteredRecipes.stream()
                .map(this::mapToServiceModel).collect(toList());
    }

    @Override
    public List<String> getRecipeTypesAsString() {
        return Arrays.stream(Recipe.Type.values())
                .map(Enum::toString)
                .collect(toList());
    }

    @Override
    public List<String> getRecipeFilterOptions() {
        List<String> filterOptions = new ArrayList<>();
        filterOptions.add("ALL");
        filterOptions.addAll(getRecipeTypesAsString());
        return filterOptions;
    }

    @Override
    @Transactional
    public void upload(RecipeUploadServiceModel uploadModel) {

        Image image = null;

        try {
            final Recipe recipe = mappingService.map(uploadModel, Recipe.class);

            List<String> applianceIds = uploadModel.getApplianceIds();
            List<Appliance> appliances = applianceRepository.findAllByIdIn(applianceIds);

            String ingredientList = uploadModel.getIngredientList();
            List<IngredientCreateServiceModel> ingredientCreateModels =
                    extractIngredientCreateModels(ingredientList);
            List<Ingredient> ingredients = mappingService.mapAll(ingredientCreateModels, Ingredient.class);
            ingredients.forEach(i -> i.setRecipe(recipe));

            Profile profile = userService.getUserProfile(uploadModel.getUploaderUsername());
            image = cloudinaryService.uploadImage(uploadModel.getImage());

            recipe.setAppliances(appliances);
            recipe.setIngredients(ingredients);
            recipe.setImage(image);
            recipe.setUploader(profile);

            recipeRepository.save(recipe);
            ingredientRepository.saveAll(ingredients);
        } catch (Exception e) {
            // Add rollback behaviour for cloudinary image upload
            if (image != null) {
                cloudinaryService.deleteImage(image.getPublicId());
            }
            throw e;
        }
    }

    private List<IngredientCreateServiceModel> extractIngredientCreateModels(String ingredientList) {
        String[][] ingredientsData =
                parseData(ingredientList, System.lineSeparator(), INGREDIENT_TEXT_AREA_VALUE_SEPARATOR);
        return ingredientService.mapToCreateModels(ingredientsData);
    }

    private String resolveIconUrl(Recipe.Type type) {
        return String.format(RECIPE_TYPE_ICON_URL_TEMPLATE, type.toString().toLowerCase());
    }

    private String getUploaderUsername(Recipe recipe) {
        return recipe.getUploader().getUser().getUsername();
    }

    private RecipeServiceModel mapToServiceModel(Recipe recipe) {
        RecipeServiceModel serviceModel = new RecipeServiceModel();
        serviceModel.setTitle(recipe.getTitle());
        serviceModel.setUploaderUsername(getUploaderUsername(recipe));
        serviceModel.setTypeIconUrl(resolveIconUrl(recipe.getType()));
        serviceModel.setCanModify(true);
        return serviceModel;
    }
}
