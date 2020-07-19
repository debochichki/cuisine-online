package com.softuni.cuisineonline.web.controllers;

import com.softuni.cuisineonline.service.models.video.VideoEditServiceModel;
import com.softuni.cuisineonline.service.models.video.VideoUploadServiceModel;
import com.softuni.cuisineonline.service.services.domain.VideoService;
import com.softuni.cuisineonline.service.services.util.MappingService;
import com.softuni.cuisineonline.web.models.video.VideoDeleteFormModel;
import com.softuni.cuisineonline.web.models.video.VideoEditFormModel;
import com.softuni.cuisineonline.web.models.video.VideoUploadFormModel;
import com.softuni.cuisineonline.web.models.video.VideoViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/videos")
public class VideoController {

    private final MappingService mappingService;
    private final VideoService videoService;

    public VideoController(MappingService mappingService, VideoService videoService) {
        this.mappingService = mappingService;
        this.videoService = videoService;
    }

    @GetMapping("/all")
    public ModelAndView getAllVideos(ModelAndView modelAndView) {
        modelAndView.setViewName("video/all-videos");
        List<VideoViewModel> viewModels =
                mappingService.mapAll(videoService.getAll(), VideoViewModel.class);
        modelAndView.addObject("videos", viewModels);
        return modelAndView;
    }

    @GetMapping("/upload")
    public String getUploadForm() {
        return "video/upload-video";
    }

    @GetMapping("/view/{id}")
    public ModelAndView getVideoView(@PathVariable String id, ModelAndView modelAndView) {
        modelAndView.setViewName("video/view-video");
        addViewModel(id, modelAndView);
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView getEditForm(@PathVariable String id, ModelAndView modelAndView) {
        modelAndView.setViewName("video/edit-video");
        addViewModel(id, modelAndView);
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView getDeleteForm(@PathVariable String id, ModelAndView modelAndView) {
        modelAndView.setViewName("video/delete-video");
        addViewModel(id, modelAndView);
        return modelAndView;
    }

    @PostMapping("/upload")
    public String uploadVideo(@ModelAttribute VideoUploadFormModel uploadModel, HttpSession session) {
        String loggedInUserUsername = (String) session.getAttribute("username");
        VideoUploadServiceModel serviceModel =
                mappingService.map(uploadModel, VideoUploadServiceModel.class);
        serviceModel.setUploaderUsername(loggedInUserUsername);
        videoService.upload(serviceModel);
        return "redirect:/videos/all";
    }

    @PostMapping("/edit")
    public String editVideo(@ModelAttribute VideoEditFormModel editModel) {
        VideoEditServiceModel serviceModel =
                mappingService.map(editModel, VideoEditServiceModel.class);
        videoService.edit(serviceModel);
        return "redirect:/videos/all";
    }

    @PostMapping("/delete")
    public String deleteVideo(@ModelAttribute VideoDeleteFormModel deleteMode) {
        videoService.delete(deleteMode.getId());
        return "redirect:/videos/all";
    }

    private void addViewModel(String id, ModelAndView modelAndView) {
        VideoViewModel viewModel =
                mappingService.map(videoService.getById(id), VideoViewModel.class);
        modelAndView.addObject("video", viewModel);
    }
}
