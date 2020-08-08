package com.softuni.cuisineonline.web.view.controllers;

import com.softuni.cuisineonline.service.services.domain.UserService;
import com.softuni.cuisineonline.service.services.util.MappingService;
import com.softuni.cuisineonline.web.view.controllers.base.BaseController;
import com.softuni.cuisineonline.web.view.models.user.ProfileViewModel;
import com.softuni.cuisineonline.web.view.models.user.UserRoleViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

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
    public ModelAndView getProfile(ModelAndView modelAndView, Principal principal) {
        modelAndView.setViewName("user/profile");
        String username = principal.getName();
        ProfileViewModel viewModel =
                mappingService.map(userService.getUserProfile(username), ProfileViewModel.class);
        modelAndView.addObject("profile", viewModel);
        return modelAndView;
    }

    @GetMapping("/all")
    public ModelAndView getAllUsers(ModelAndView modelAndView) {
        modelAndView.setViewName("user/all-users");
        List<UserRoleViewModel> viewModels =
                mappingService.mapAll(userService.getAllUsers(), UserRoleViewModel.class);
        modelAndView.addObject("users", viewModels);
        return modelAndView;
    }

    @PostMapping("/promote/{id}")
    public String promote(@PathVariable String id) {
        userService.promoteToAdmin(id);
        return redirect("/users/all");
    }

    @PostMapping("/demote/{id}")
    public String demote(@PathVariable String id) {
        userService.demoteToUser(id);
        return redirect("/users/all");
    }
}
