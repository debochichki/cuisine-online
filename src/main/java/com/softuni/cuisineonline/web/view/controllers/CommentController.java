package com.softuni.cuisineonline.web.view.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/comments")
public class CommentController {

    @GetMapping("/all")
    public String getAllComments() {
        return "comment/all-comments.html";
    }
}
