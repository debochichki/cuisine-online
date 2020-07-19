package com.softuni.cuisineonline.service.services.domain;

import com.softuni.cuisineonline.service.models.video.VideoEditServiceModel;
import com.softuni.cuisineonline.service.models.video.VideoServiceModel;
import com.softuni.cuisineonline.service.models.video.VideoUploadServiceModel;

import java.util.List;

public interface VideoService {

    void upload(VideoUploadServiceModel uploadModel);

    List<VideoServiceModel> getAll();

    VideoServiceModel getById(String id);

    void edit(VideoEditServiceModel editModel);

    void delete(String id);
}
