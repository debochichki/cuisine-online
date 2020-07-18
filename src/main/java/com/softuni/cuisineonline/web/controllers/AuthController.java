package com.softuni.cuisineonline.web.controllers;

import com.softuni.cuisineonline.service.models.UserLoginServiceModel;
import com.softuni.cuisineonline.service.models.UserRegisterServiceModel;
import com.softuni.cuisineonline.service.services.domain.AuthService;
import com.softuni.cuisineonline.service.services.util.MappingService;
import com.softuni.cuisineonline.web.models.UserLoginFormModel;
import com.softuni.cuisineonline.web.models.UserRegisterFormModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class AuthController {

    private final MappingService mappingService;
    private final AuthService authService;

    public AuthController(MappingService mappingService, AuthService authService) {
        this.mappingService = mappingService;
        this.authService = authService;
    }

    @GetMapping("/login")
    public String getLoginForm() {
        return "auth/login.html";
    }

    @GetMapping("/register")
    public String getRegisterForm() {
        return "auth/register.html";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute UserLoginFormModel userLoginModel, HttpSession session) {
        UserLoginServiceModel serviceModel =
                mappingService.map(userLoginModel, UserLoginServiceModel.class);
        String userUsername = authService.login(serviceModel);
        session.setAttribute("username", userUsername);
        return "redirect:/home";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserRegisterFormModel userRegisterModel) {
        UserRegisterServiceModel serviceModel =
                mappingService.map(userRegisterModel, UserRegisterServiceModel.class);
        authService.register(serviceModel);
        return "redirect:/login";
    }

}
