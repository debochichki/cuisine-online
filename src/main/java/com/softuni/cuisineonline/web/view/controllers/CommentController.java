package com.softuni.cuisineonline.web.view.controllers;

import com.softuni.cuisineonline.service.models.comment.CommentEditServiceModel;
import com.softuni.cuisineonline.service.models.comment.CommentServiceModel;
import com.softuni.cuisineonline.service.services.domain.CommentService;
import com.softuni.cuisineonline.service.services.util.MappingService;
import com.softuni.cuisineonline.web.view.controllers.base.BaseController;
import com.softuni.cuisineonline.web.view.models.comment.CommentEditFormModel;
import com.softuni.cuisineonline.web.view.models.comment.CommentEditViewModel;
import com.softuni.cuisineonline.web.view.models.comment.CommentPostFormModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.time.Instant;

@Controller
@RequestMapping("/comments")
public class CommentController extends BaseController {

    private final CommentService commentService;
    private final MappingService mappingService;

    public CommentController(
            CommentService commentService,
            MappingService mappingService) {
        this.commentService = commentService;
        this.mappingService = mappingService;
    }

    @GetMapping("/all")
    public String getCommentsView() {
        return "comment/all-comments.html";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView getEditForm(@PathVariable String id, ModelAndView modelAndView) {
        modelAndView.setViewName("comment/edit-comment");
        CommentEditViewModel viewModel =
                mappingService.map(commentService.getById(id), CommentEditViewModel.class);
        modelAndView.addObject("comment", viewModel);
        return modelAndView;
    }

    @PostMapping("/post")
    public String postComment(@ModelAttribute CommentPostFormModel postModel, Principal principal) {
        String content = postModel.getContent();
        String username = principal.getName();
        CommentServiceModel serviceModel = buildServiceModel(content, username);
        commentService.post(serviceModel);
        return redirect("/comments/all");
    }

    @PostMapping("/edit")
    public String editComment(@ModelAttribute CommentEditFormModel editModel) {
        CommentEditServiceModel serviceModel =
                mappingService.map(editModel, CommentEditServiceModel.class);
        commentService.edit(serviceModel);
        return redirect("/comments/all");
    }

    @PostMapping("/delete/{id}")
    public String deleteComment(@PathVariable String id) {
        commentService.delete(id);
        return redirect("/comments/all");
    }

    private CommentServiceModel buildServiceModel(String content, String username) {
        CommentServiceModel serviceModel = new CommentServiceModel();
        serviceModel.setContent(content);
        serviceModel.setUploaderUsername(username);
        serviceModel.setInstant(Instant.now());
        return serviceModel;
    }
}
