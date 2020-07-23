package com.softuni.cuisineonline.service.services.domain;

import com.softuni.cuisineonline.service.models.comment.CommentServiceModel;

import java.util.List;

public interface CommentService {

    List<CommentServiceModel> getAll();
}
