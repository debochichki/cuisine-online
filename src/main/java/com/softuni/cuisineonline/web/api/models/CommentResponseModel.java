package com.softuni.cuisineonline.web.api.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponseModel {

    private String content;

    private Instant instant;

    private String uploaderUsername;
}
