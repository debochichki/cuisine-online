package com.softuni.cuisineonline.service.services.validation;

import com.softuni.cuisineonline.service.models.video.VideoEditServiceModel;
import com.softuni.cuisineonline.service.models.video.VideoUploadServiceModel;

public interface VideoValidationService {

    void validateUploadModel(VideoUploadServiceModel uploadModel);

    void validateEditModel(VideoEditServiceModel editModel);
}
