package com.softuni.cuisineonline.web.models.video;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VideoViewModel {

    private String id;

    private String title;

    private String url;

    private String uploaderUsername;
}
