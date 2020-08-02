package com.softuni.cuisineonline.web.view.controllers;

import com.softuni.cuisineonline.service.services.domain.UserService;
import com.softuni.cuisineonline.service.services.util.MappingService;
import com.softuni.cuisineonline.web.view.controllers.base.BaseController;
import com.softuni.cuisineonline.web.view.models.user.UserViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UserController extends BaseController {

    private final UserService userService;
    private final MappingService mappingService;

    public UserController(UserService userService,
                          MappingService mappingService) {
        this.userService = userService;
        this.mappingService = mappingService;
    }

    @GetMapping("/profile")
    public ModelAndView getProfile(ModelAndView modelAndView, HttpSession session) {
        modelAndView.setViewName("user/profile");
        String username = (String) session.getAttribute("username");
        UserViewModel viewModel =
                mappingService.map(userService.getUserDetails(username), UserViewModel.class);
        modelAndView.addObject("profile", viewModel);
        return modelAndView;
    }

    @GetMapping("/all")
    public String getAllUsers() {
        return "user/all-users.html";
    }
}
