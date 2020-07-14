package com.softuni.cuisineonline.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String getLoginForm() {
        return "auth/login.html";
    }

    @GetMapping("/register")
    public String getRegisterForm() {
        return "auth/register.html";
    }

}
