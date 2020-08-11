package com.softuni.cuisineonline.service.services.domain.impl;

import com.softuni.cuisineonline.data.models.*;
import com.softuni.cuisineonline.data.repositories.ApplianceRepository;
import com.softuni.cuisineonline.data.repositories.IngredientRepository;
import com.softuni.cuisineonline.data.repositories.RecipeRepository;
import com.softuni.cuisineonline.data.repositories.UserRepository;
import com.softuni.cuisineonline.errors.MissingEntityException;
import com.softuni.cuisineonline.service.models.ingredient.IngredientCreateServiceModel;
import com.softuni.cuisineonline.service.models.recipe.RecipeBaseServiceModel;
import com.softuni.cuisineonline.service.models.recipe.RecipeEditServiceModel;
import com.softuni.cuisineonline.service.models.recipe.RecipeServiceModel;
import com.softuni.cuisineonline.service.models.recipe.RecipeUploadServiceModel;
import com.softuni.cuisineonline.service.services.domain.*;
import com.softuni.cuisineonline.service.services.util.MappingService;
import com.softuni.cuisineonline.service.services.validation.RecipeValidationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.softuni.cuisineonline.service.services.util.InputUtil.parseData;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private static final String RECIPE_TYPE_ICON_URL_TEMPLATE = "/icon/%s_icon.png";
    private static final String INGREDIENT_TEXT_AREA_VALUE_SEPARATOR = ",";
    private static final String ALL_RECIPES_FILTER_OPTION = "ALL";
    private final RecipeRepository recipeRepository;
    private final ApplianceRepository applianceRepository;
    private final IngredientRepository ingredientRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ImageService imageService;
    private final IngredientService ingredientService;
    private final MappingService mappingService;
    private final CloudinaryService cloudinaryService;
    private final RecipeValidationService validationService;
    private final AuthenticatedUserFacade authenticationFacade;

    @Override
    public RecipeServiceModel getById(String id) {
        Recipe recipe = getRecipeById(id);
        RecipeServiceModel serviceModel = mappingService.map(recipe, RecipeServiceModel.class);
        serviceModel.setUploaderUsername(getUploaderUsername(recipe));
        return serviceModel;
    }

    @Override
    public List<RecipeBaseServiceModel> getAllByFilterOption(String filterOption) {
        List<Recipe> filteredRecipes = filterOption.equals(ALL_RECIPES_FILTER_OPTION)
                ? recipeRepository.findAll()
                : recipeRepository
                .findAllByTypeOrderByTitleAsc(Recipe.Type.valueOf(filterOption));

        return filteredRecipes.stream()
                .map(this::mapToServiceModel).collect(toList());
    }

    @Override
    public List<String> getRecipeTypesAsStringValues() {
        return Arrays.stream(Recipe.Type.values())
                .map(Enum::toString)
                .collect(toList());
    }

    @Override
    public List<String> getRecipeFilterOptions() {
        List<String> filterOptions = new ArrayList<>();
        filterOptions.add("ALL");
        filterOptions.addAll(getRecipeTypesAsStringValues());
        return filterOptions;
    }

    @Override
    @Transactional
    public void upload(RecipeUploadServiceModel uploadModel) {
        validationService.validateUploadModel(uploadModel);

        final Recipe recipe = mappingService.map(uploadModel, Recipe.class);

        List<String> applianceIds = uploadModel.getApplianceIds();
        if (applianceIds == null) {
            applianceIds = new ArrayList<>();
        }
        List<Appliance> appliances = applianceRepository.findAllByIdIn(applianceIds);

        String ingredientsData = uploadModel.getIngredientsData();
        List<Ingredient> ingredients = buildIngredients(recipe, ingredientsData);
        Image image = null;
        Profile profile = getUserByUsername(uploadModel.getUploaderUsername()).getProfile();

        try {
            image = cloudinaryService.uploadImage(uploadModel.getImage());

            recipe.setAppliances(appliances);
            recipe.setIngredients(ingredients);
            recipe.setImage(image);
            recipe.setUploader(profile);

            recipeRepository.save(recipe);
            ingredientRepository.saveAll(ingredients);
        } catch (Exception e) {
            // Add rollback behavior for cloudinary image upload
            handleImageRollback(image);
            throw e;
        }
    }

    @Override
    @Transactional
    public void edit(RecipeEditServiceModel editModel) {
        validationService.validateEditModel(editModel);

        final Recipe recipe = getRecipeById(editModel.getId());

        List<String> applianceIds = editModel.getApplianceIds();
        List<Appliance> newAppliances = applianceRepository.findAllByIdIn(applianceIds);

        String ingredientsData = editModel.getIngredientsData();
        List<Ingredient> newIngredients = buildIngredients(recipe, ingredientsData);
        List<Ingredient> oldIngredients = recipe.getIngredients();

        MultipartFile multipartFile = editModel.getImage();
        Image newImage = null;
        Image oldImage = null;

        try {
            if (!multipartFile.isEmpty()) {
                newImage = cloudinaryService.uploadImage(multipartFile);
                oldImage = recipe.getImage();
                recipe.setImage(newImage);
            }

            recipe.setTitle(editModel.getTitle());
            recipe.setType(editModel.getType());
            recipe.setDescription(editModel.getDescription());
            recipe.setDuration(editModel.getDuration());
            recipe.setPortions(editModel.getPortions());
            recipe.setAppliances(newAppliances);
            recipe.setIngredients(newIngredients);

            recipeRepository.save(recipe);
            ingredientRepository.saveAll(newIngredients);

            // Clean up obsolete data
            ingredientRepository.deleteAll(oldIngredients);
            handleImageDeletion(oldImage);

        } catch (Exception e) {
            // Add rollback behavior for cloudinary image upload
            handleImageRollback(newImage);
            throw e;
        }
    }

    @Override
    public void deleteById(String id) {
        Recipe recipe = getRecipeById(id);
        Image obsoleteImage = recipe.getImage();
        recipeRepository.deleteById(id);
        handleImageDeletion(obsoleteImage);
    }

    @Override
    public List<RecipeServiceModel> getRandomRecipes(int numberOfRecipes) {
        // ToDo: Fix - it returns duplicates
        List<Recipe> all = recipeRepository.findAll();
        if (all.size() < numberOfRecipes) {
            numberOfRecipes = all.size();
        }

        List<RecipeServiceModel> serviceModels = new ArrayList<>();
        Random random = new Random();
        int counter = 0;
        while (counter < numberOfRecipes) {
            int rndIndex = random.nextInt(all.size());
            RecipeServiceModel serviceModel =
                    mappingService.map(all.get(rndIndex), RecipeServiceModel.class);
            if (!serviceModels.contains(serviceModel)) {
                serviceModels.add(serviceModel);
                counter++;
            }
        }

        return serviceModels;
    }

    private void handleImageRollback(Image image) {
        if (image != null) {
            cloudinaryService.deleteImage(image.getPublicId());
        }
    }

    private void handleImageDeletion(Image image) {
        try {
            if (image != null) {
                cloudinaryService.deleteImage(image.getPublicId());
                imageService.deleteById(image.getId());
            }
        } catch (Exception ignored) {
            // Add logging that the deletion of the image failed
        }
    }

    private List<Ingredient> buildIngredients(final Recipe recipe, String ingredientsData) {
        String[][] parsedData =
                parseData(ingredientsData, System.lineSeparator(),
                        INGREDIENT_TEXT_AREA_VALUE_SEPARATOR);
        List<IngredientCreateServiceModel> ingredientCreateModels =
                ingredientService.mapToCreateModels(parsedData);

        return mappingService
                .mapAll(ingredientCreateModels, Ingredient.class)
                .stream()
                .peek(i -> i.setRecipe(recipe))
                .collect(toList());
    }

    private String resolveIconUrl(Recipe.Type type) {
        return String.format(RECIPE_TYPE_ICON_URL_TEMPLATE, type.toString().toLowerCase());
    }

    private User getUserByUsername(final String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new MissingEntityException("No user with username: " + username));
    }

    private String getUploaderUsername(Recipe recipe) {
        return recipe.getUploader().getUser().getUsername();
    }

    private RecipeBaseServiceModel mapToServiceModel(Recipe recipe) {
        RecipeBaseServiceModel serviceModel = mappingService.map(recipe, RecipeBaseServiceModel.class);
        String uploaderUsername = getUploaderUsername(recipe);
        serviceModel.setUploaderUsername(uploaderUsername);
        serviceModel.setTypeIconUrl(resolveIconUrl(recipe.getType()));
        String principalName = authenticationFacade.getPrincipalName();
        boolean canModify = userService.canModify(principalName, uploaderUsername);
        serviceModel.setCanModify(canModify);
        return serviceModel;
    }


    private Recipe getRecipeById(String id) {
        return recipeRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("No recipe found in the database."));
    }
}
