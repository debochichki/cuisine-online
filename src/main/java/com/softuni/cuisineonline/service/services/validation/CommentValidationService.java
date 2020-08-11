package com.softuni.cuisineonline.service.services.validation;

import com.softuni.cuisineonline.service.models.comment.CommentEditServiceModel;
import com.softuni.cuisineonline.service.models.comment.CommentServiceModel;

public interface CommentValidationService {

    void validatePostModel(CommentServiceModel serviceModel);

    void validateEditModel(CommentEditServiceModel serviceModel);
}
