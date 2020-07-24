package com.softuni.cuisineonline.service.services.domain;

import com.softuni.cuisineonline.service.models.comment.CommentEditServiceModel;
import com.softuni.cuisineonline.service.models.comment.CommentServiceModel;

import java.util.List;

public interface CommentService {

    List<CommentServiceModel> getAll();

    CommentServiceModel getById(String id);

    void post(CommentServiceModel serviceModel);

    void edit(CommentEditServiceModel serviceModel);

    void delete(String id);
}
