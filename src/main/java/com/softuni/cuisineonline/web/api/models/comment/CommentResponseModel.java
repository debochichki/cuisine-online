package com.softuni.cuisineonline.web.api.models.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponseModel {

    private String id;

    private String content;

    private Instant instant;

    private String uploaderUsername;

    private boolean canModify;
}
