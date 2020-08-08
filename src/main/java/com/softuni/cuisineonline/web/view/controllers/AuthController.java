package com.softuni.cuisineonline.web.view.controllers;

import com.softuni.cuisineonline.service.models.auth.UserRegisterServiceModel;
import com.softuni.cuisineonline.service.services.domain.AuthService;
import com.softuni.cuisineonline.service.services.util.MappingService;
import com.softuni.cuisineonline.web.view.controllers.base.BaseController;
import com.softuni.cuisineonline.web.view.models.auth.UserRegisterFormModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController extends BaseController {

    private final MappingService mappingService;
    private final AuthService authService;

    public AuthController(MappingService mappingService, AuthService authService) {
        this.mappingService = mappingService;
        this.authService = authService;
    }

    @GetMapping("/login")
    public String getLoginForm(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", error);
        }

        return "auth/login.html";
    }

    @GetMapping("/register")
    public String getRegisterForm() {
        return "auth/register.html";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserRegisterFormModel registerModel) {
        UserRegisterServiceModel serviceModel =
                mappingService.map(registerModel, UserRegisterServiceModel.class);
        authService.register(serviceModel);
        return redirect("/login");
    }

}
