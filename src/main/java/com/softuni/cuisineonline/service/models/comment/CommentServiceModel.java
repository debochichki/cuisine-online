package com.softuni.cuisineonline.service.models.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class CommentServiceModel {

    private String content;

    private Instant instant;

    private String uploaderUsername;
}
