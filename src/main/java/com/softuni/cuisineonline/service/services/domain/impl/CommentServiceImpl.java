package com.softuni.cuisineonline.service.services.domain.impl;

import com.softuni.cuisineonline.data.models.Comment;
import com.softuni.cuisineonline.data.models.Profile;
import com.softuni.cuisineonline.data.repositories.CommentRepository;
import com.softuni.cuisineonline.service.models.comment.CommentEditServiceModel;
import com.softuni.cuisineonline.service.models.comment.CommentServiceModel;
import com.softuni.cuisineonline.service.services.domain.CommentService;
import com.softuni.cuisineonline.service.services.domain.UserService;
import com.softuni.cuisineonline.service.services.util.MappingService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final MappingService mappingService;
    private final UserService userService;

    public CommentServiceImpl(
            CommentRepository commentRepository,
            MappingService mappingService,
            UserService userService) {
        this.commentRepository = commentRepository;
        this.mappingService = mappingService;
        this.userService = userService;
    }

    @Override
    public List<CommentServiceModel> getAll() {
        // ToDo: Set the canModify field!
        final List<CommentServiceModel> allComments = commentRepository.findAllByOrderByInstantAsc()
                .stream()
                .map(c -> {
                    CommentServiceModel serviceModel = mappingService.map(c, CommentServiceModel.class);
                    String username = getUploaderUsername(c);
                    serviceModel.setUploaderUsername(username);
                    return serviceModel;
                }).collect(toList());

        return allComments;
    }

    @Override
    public CommentServiceModel getById(String id) {
        return mappingService.map(getCommentById(id), CommentServiceModel.class);
    }

    @Override
    // ToDo: Add interceptor to set the profile!!!
    public void post(CommentServiceModel serviceModel) {
        Comment comment = mappingService.map(serviceModel, Comment.class);
        Profile profile = userService.getUserProfile(serviceModel.getUploaderUsername());
        comment.setUploader(profile);
        commentRepository.save(comment);
    }

    @Override
    public void edit(CommentEditServiceModel serviceModel) {
        Comment comment = getCommentById(serviceModel.getId());
        comment.setContent(serviceModel.getContent());
        commentRepository.save(comment);
    }

    @Override
    public void delete(String id) {
        Comment comment = getCommentById(id);
        commentRepository.delete(comment);
    }

    private Comment getCommentById(String id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("No comment found in the database."));
    }

    private String getUploaderUsername(Comment comment) {
        return comment.getUploader().getUser().getUsername();
    }
}
