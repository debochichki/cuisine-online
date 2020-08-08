package com.softuni.cuisineonline.service.services.domain.impl;

import com.softuni.cuisineonline.data.models.Comment;
import com.softuni.cuisineonline.data.models.Profile;
import com.softuni.cuisineonline.data.models.User;
import com.softuni.cuisineonline.data.repositories.CommentRepository;
import com.softuni.cuisineonline.data.repositories.UserRepository;
import com.softuni.cuisineonline.errors.MissingEntityException;
import com.softuni.cuisineonline.service.models.comment.CommentEditServiceModel;
import com.softuni.cuisineonline.service.models.comment.CommentServiceModel;
import com.softuni.cuisineonline.service.services.domain.CommentService;
import com.softuni.cuisineonline.service.services.util.MappingService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final MappingService mappingService;
    private final UserRepository userRepository;

    public CommentServiceImpl(
            CommentRepository commentRepository,
            MappingService mappingService,
            UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.mappingService = mappingService;
        this.userRepository = userRepository;
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
    public void post(CommentServiceModel serviceModel) {
        Comment comment = mappingService.map(serviceModel, Comment.class);
        Profile profile = getUserByUsername(serviceModel.getUploaderUsername()).getProfile();
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

    private User getUserByUsername(final String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new MissingEntityException("No user with username: " + username));
    }

    private Comment getCommentById(String id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("No comment found in the database."));
    }

    private String getUploaderUsername(Comment comment) {
        return comment.getUploader().getUser().getUsername();
    }
}
