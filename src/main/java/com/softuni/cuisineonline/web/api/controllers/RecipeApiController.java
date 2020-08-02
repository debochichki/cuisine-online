package com.softuni.cuisineonline.web.api.controllers;

import com.softuni.cuisineonline.service.services.domain.RecipeService;
import com.softuni.cuisineonline.service.services.util.MappingService;
import com.softuni.cuisineonline.web.api.controllers.base.BaseApiController;
import com.softuni.cuisineonline.web.api.models.recipe.RecipeResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RecipeApiController extends BaseApiController {

    private final MappingService mappingService;
    private final RecipeService recipeService;

    public RecipeApiController(
            MappingService mappingService,
            RecipeService recipeService) {
        this.mappingService = mappingService;
        this.recipeService = recipeService;
    }

    @GetMapping("/recipes/types")
    public ResponseEntity<List<String>> getAllTypes() {
        final List<String> typesAsStrings = recipeService.getRecipeTypesAsStringValues();
        return new ResponseEntity<>(typesAsStrings, HttpStatus.OK);
    }

    @GetMapping("/recipes/filter")
    public ResponseEntity<List<String>> getFilterOptions() {
        final List<String> filterOptions = recipeService.getRecipeFilterOptions();
        return new ResponseEntity<>(filterOptions, HttpStatus.OK);
    }

    @GetMapping("/recipes")
    public ResponseEntity<List<RecipeResponseModel>> getAllRecipes(
            @RequestParam(defaultValue = "ALL") String filterOption) {
        final List<RecipeResponseModel> responseModels =
                mappingService.mapAll(recipeService
                        .getAllByFilterOption(filterOption), RecipeResponseModel.class);
        return new ResponseEntity<>(responseModels, HttpStatus.OK);
    }
}
