package com.softuni.cuisineonline.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/profile")
    public String getProfile() {
        return "user/profile.html";
    }

    @GetMapping("/users")
    public String getAllUsers() {
        return "user/all-users.html";
    }
}
