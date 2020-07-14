package com.softuni.cuisineonline.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/videos")
public class VideoController {

    @GetMapping("/all")
    public String getAllVideos() {
        return "video/all-videos.html";
    }
}
