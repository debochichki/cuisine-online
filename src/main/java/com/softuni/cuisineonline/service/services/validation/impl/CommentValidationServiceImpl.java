package com.softuni.cuisineonline.service.services.validation.impl;

import com.softuni.cuisineonline.errors.ServerException;
import com.softuni.cuisineonline.errors.ValidationException;
import com.softuni.cuisineonline.service.models.comment.CommentEditServiceModel;
import com.softuni.cuisineonline.service.models.comment.CommentServiceModel;
import com.softuni.cuisineonline.service.services.validation.CommentValidationService;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static com.softuni.cuisineonline.service.services.util.Constants.COMMENT_CONTENT_LENGTH_LOWER_BOUND;
import static com.softuni.cuisineonline.service.services.util.Constants.COMMENT_CONTENT_LENGTH_UPPER_BOUND;
import static com.softuni.cuisineonline.service.services.util.InputUtil.isLengthInBounds;

@Service
public class CommentValidationServiceImpl implements CommentValidationService {

    @Override
    public void validatePostModel(CommentServiceModel serviceModel) {
        String uploaderUsername = serviceModel.getUploaderUsername();
        Instant instant = serviceModel.getInstant();
        String content = serviceModel.getContent();

        if (uploaderUsername == null) {
            throw new ServerException("Error posting comment. Uploader username is null.");
        }

        if (instant == null) {
            throw new ServerException("Error posting comment. Date of posting is null.");
        }

        validateContentLength(content);
    }

    @Override
    public void validateEditModel(CommentEditServiceModel serviceModel) {
        String content = serviceModel.getContent();
        validateContentLength(content);
    }

    private void validateContentLength(String content) {
        if (!isLengthInBounds(content, COMMENT_CONTENT_LENGTH_LOWER_BOUND, COMMENT_CONTENT_LENGTH_UPPER_BOUND)) {
            throw new ValidationException(
                    String.format("Comment content length needs to be between %d and %d symbols",
                            COMMENT_CONTENT_LENGTH_LOWER_BOUND,
                            COMMENT_CONTENT_LENGTH_UPPER_BOUND));
        }
    }
}
