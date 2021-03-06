package com.softuni.cuisineonline.web.api.controllers;

import com.softuni.cuisineonline.service.services.domain.CommentService;
import com.softuni.cuisineonline.service.services.util.MappingService;
import com.softuni.cuisineonline.web.api.models.comment.CommentResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentApiController {

    private final CommentService commentService;
    private final MappingService mappingService;

    public CommentApiController(
            CommentService commentService,
            MappingService mappingService) {
        this.commentService = commentService;
        this.mappingService = mappingService;
    }

    @GetMapping("/comments/all")
    public ResponseEntity<List<CommentResponseModel>> getAllComments() {
        final List<CommentResponseModel> responseModels =
                mappingService.mapAll(commentService.getAll(), CommentResponseModel.class);
        return new ResponseEntity<>(responseModels, HttpStatus.OK);
    }
}
