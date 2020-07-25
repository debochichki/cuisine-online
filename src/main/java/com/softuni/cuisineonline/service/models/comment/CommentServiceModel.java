package com.softuni.cuisineonline.service.models.comment;

import com.softuni.cuisineonline.service.models.base.BaseServiceModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class CommentServiceModel extends BaseServiceModel {

    private String content;

    private Instant instant;

    private String uploaderUsername;

    private boolean canModify;
}
