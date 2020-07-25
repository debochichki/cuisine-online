package com.softuni.cuisineonline.web.view.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

    @GetMapping("/profile")
    public String getProfile() {
        return "user/profile.html";
    }

    @GetMapping("/all")
    public String getAllUsers() {
        return "user/all-users.html";
    }
}
