package com.softuni.cuisineonline.web.view.controllers;

import com.softuni.cuisineonline.service.models.recipe.RecipeEditServiceModel;
import com.softuni.cuisineonline.service.models.recipe.RecipeUploadServiceModel;
import com.softuni.cuisineonline.service.services.domain.ApplianceService;
import com.softuni.cuisineonline.service.services.domain.RecipeService;
import com.softuni.cuisineonline.service.services.util.MappingService;
import com.softuni.cuisineonline.web.view.controllers.base.BaseController;
import com.softuni.cuisineonline.web.view.models.appliance.ApplianceViewModel;
import com.softuni.cuisineonline.web.view.models.recipe.RecipeDeleteFormModel;
import com.softuni.cuisineonline.web.view.models.recipe.RecipeEditFormModel;
import com.softuni.cuisineonline.web.view.models.recipe.RecipeUploadFormModel;
import com.softuni.cuisineonline.web.view.models.recipe.RecipeViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/recipes")
public class RecipeController extends BaseController {

    private final RecipeService recipeService;
    private final ApplianceService applianceService;
    private final MappingService mappingService;

    public RecipeController(
            RecipeService recipeService,
            ApplianceService applianceService,
            MappingService mappingService) {
        this.recipeService = recipeService;
        this.applianceService = applianceService;
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

    @GetMapping("/view/{id}")
    public ModelAndView getRecipeView(@PathVariable String id, ModelAndView modelAndView) {
        modelAndView.setViewName("recipe/view-recipe");
        addViewModel(id, modelAndView);
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView getEditForm(@PathVariable String id, ModelAndView modelAndView) {
        modelAndView.setViewName("recipe/edit-recipe");
        addViewModel(id, modelAndView);
        modelAndView.addObject("types", recipeService.getRecipeTypesAsStringValues());
        List<ApplianceViewModel> allAppliances = mappingService
                .mapAll(applianceService.getAll(), ApplianceViewModel.class);
        modelAndView.addObject("allAppliances", allAppliances);
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView getDeleteForm(@PathVariable String id, ModelAndView modelAndView) {
        modelAndView.setViewName("recipe/delete-recipe");
        addViewModel(id, modelAndView);
        return modelAndView;
    }

    @PostMapping("/upload")
    public String uploadRecipe(@ModelAttribute RecipeUploadFormModel uploadModel,
                               Principal principal) {
        RecipeUploadServiceModel serviceModel =
                mappingService.map(uploadModel, RecipeUploadServiceModel.class);
        String username = principal.getName();
        serviceModel.setUploaderUsername(username);
        recipeService.upload(serviceModel);
        return redirect("/recipes");
    }

    @PostMapping("/edit")
    public String editRecipe(@ModelAttribute RecipeEditFormModel editModel) {
        RecipeEditServiceModel serviceModel =
                mappingService.map(editModel, RecipeEditServiceModel.class);
        recipeService.edit(serviceModel);
        return redirect("/recipes");
    }

    @PostMapping("/delete")
    public String deleteRecipe(@ModelAttribute RecipeDeleteFormModel deleteModel) {
        recipeService.deleteById(deleteModel.getId());
        return redirect("/recipes");
    }

    private void addViewModel(@PathVariable String id, ModelAndView modelAndView) {
        RecipeViewModel viewModel =
                mappingService.map(recipeService.getById(id), RecipeViewModel.class);
        modelAndView.addObject("recipe", viewModel);
    }
}
