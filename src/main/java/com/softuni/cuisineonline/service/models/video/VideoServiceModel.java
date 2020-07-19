package com.softuni.cuisineonline.service.models.video;

import com.softuni.cuisineonline.service.models.base.BaseServiceModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VideoServiceModel extends BaseServiceModel {

    private String title;

    private String url;

    private String uploaderUsername;
}
