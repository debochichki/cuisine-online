package com.softuni.cuisineonline.service.services.domain.impl;

import com.softuni.cuisineonline.data.models.Comment;
import com.softuni.cuisineonline.data.repositories.CommentRepository;
import com.softuni.cuisineonline.service.models.comment.CommentServiceModel;
import com.softuni.cuisineonline.service.services.domain.CommentService;
import com.softuni.cuisineonline.service.services.util.MappingService;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final MappingService mappingService;

    public CommentServiceImpl(
            CommentRepository commentRepository,
            MappingService mappingService) {
        this.commentRepository = commentRepository;
        this.mappingService = mappingService;
    }

    @Override
    public List<CommentServiceModel> getAll() {
        final List<CommentServiceModel> allComments = commentRepository.findAll().stream()
                .map(c -> {
                    CommentServiceModel serviceModel = mappingService.map(c, CommentServiceModel.class);
                    String username = getUploaderUsername(c);
                    serviceModel.setUploaderUsername(username);
                    return serviceModel;
                }).collect(toList());

        return allComments;
    }

    private String getUploaderUsername(Comment comment) {
        return comment.getUploader().getUser().getUsername();
    }

}
