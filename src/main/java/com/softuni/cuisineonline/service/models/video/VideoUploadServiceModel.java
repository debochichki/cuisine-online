package com.softuni.cuisineonline.service.models.video;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VideoUploadServiceModel {

    private String title;

    private String url;

    private String uploaderUsername;
}
