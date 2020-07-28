package com.softuni.cuisineonline.web.view.controllers;

import com.softuni.cuisineonline.service.models.recipe.RecipeUploadServiceModel;
import com.softuni.cuisineonline.service.services.domain.RecipeService;
import com.softuni.cuisineonline.service.services.util.MappingService;
import com.softuni.cuisineonline.web.view.controllers.base.BaseController;
import com.softuni.cuisineonline.web.view.models.recipe.RecipeUploadFormModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/recipes")
public class RecipeController extends BaseController {

    private final RecipeService recipeService;
    private final MappingService mappingService;

    public RecipeController(
            RecipeService recipeService,
            MappingService mappingService) {
        this.recipeService = recipeService;
        this.mappingService = mappingService;
    }

    @GetMapping("")
    public ModelAndView getRecipesView(ModelAndView modelAndView) {
        modelAndView.setViewName("recipe/recipes");
        return modelAndView;
    }

    @GetMapping("/upload")
    public String getUploadForm() {
        return "recipe/upload-recipe.html";
    }

    @PostMapping("/upload")
    public String uploadRecipe(@ModelAttribute RecipeUploadFormModel uploadModel,
                               HttpSession session) {
        RecipeUploadServiceModel serviceModel =
                mappingService.map(uploadModel, RecipeUploadServiceModel.class);
        String username = (String) session.getAttribute("username");
        serviceModel.setUploaderUsername(username);
        recipeService.upload(serviceModel);
        return redirect("/recipes/all");
    }
}
