package com.softuni.cuisineonline.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/recipes")
public class RecipeController {

    @GetMapping("/all")
    public ModelAndView getAllRecipes(ModelAndView modelAndView) {
        modelAndView.setViewName("recipes");
        return modelAndView;
    }
}
