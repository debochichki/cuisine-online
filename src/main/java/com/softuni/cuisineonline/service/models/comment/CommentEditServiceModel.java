package com.softuni.cuisineonline.service.models.comment;

import com.softuni.cuisineonline.service.models.base.BaseServiceModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentEditServiceModel extends BaseServiceModel {

    private String content;
}
