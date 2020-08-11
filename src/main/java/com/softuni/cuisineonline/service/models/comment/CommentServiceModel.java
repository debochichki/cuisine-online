package com.softuni.cuisineonline.service.models.comment;

import com.softuni.cuisineonline.service.models.base.BaseServiceModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CommentServiceModel extends BaseServiceModel {

    private String content;

    private Instant instant;

    private String uploaderUsername;

    private boolean canModify;
}
