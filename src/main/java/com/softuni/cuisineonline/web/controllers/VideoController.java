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

    @GetMapping("/upload")
    public String getUploadForm() {
        return "video/upload-video";
    }

    @GetMapping("/view")
    public String getVideoView() {
        return "video/view-video";
    }

    @GetMapping("/edit")
    public String getEditForm() {
        return "video/edit-video";
    }

    @GetMapping("/delete")
    public String getDeleteForm() {
        return "video/delete-video";
    }
}
