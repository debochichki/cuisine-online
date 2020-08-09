package com.softuni.cuisineonline.web.view.controllers;

import com.softuni.cuisineonline.service.services.domain.RecipeService;
import com.softuni.cuisineonline.service.services.util.MappingService;
import com.softuni.cuisineonline.web.view.models.recipe.RecipeViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class HomeController {

    private static final int NUMBER_OF_RECIPES = 3;
    private final RecipeService recipeService;
    private final MappingService mappingService;

    public HomeController(
            RecipeService recipeService,
            MappingService mappingService) {
        this.recipeService = recipeService;
        this.mappingService = mappingService;
    }

    @GetMapping("/")
    public String getIndex() {
        return "home/index.html";
    }

    @GetMapping("/home")
    public ModelAndView getHome(ModelAndView modelAndView) {
        modelAndView.setViewName("home/home");
        List<RecipeViewModel> viewModels = mappingService.mapAll(
                recipeService.getRandomRecipes(NUMBER_OF_RECIPES), RecipeViewModel.class);
        modelAndView.addObject("recipes", viewModels);
        return modelAndView;
    }
}
