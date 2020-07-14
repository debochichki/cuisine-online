package com.softuni.cuisineonline.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/profile")
    public String getProfile() {
        return "profile.html";
    }
}
