package com.softuni.cuisineonline.service.services.domain.impl;

import com.softuni.cuisineonline.data.models.Profile;
import com.softuni.cuisineonline.data.models.Video;
import com.softuni.cuisineonline.data.repositories.VideoRepository;
import com.softuni.cuisineonline.errors.MissingEntityException;
import com.softuni.cuisineonline.service.models.video.VideoEditServiceModel;
import com.softuni.cuisineonline.service.models.video.VideoServiceModel;
import com.softuni.cuisineonline.service.models.video.VideoUploadServiceModel;
import com.softuni.cuisineonline.service.services.domain.UserService;
import com.softuni.cuisineonline.service.services.domain.VideoService;
import com.softuni.cuisineonline.service.services.util.MappingService;
import com.softuni.cuisineonline.service.services.validation.VideoValidationService;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.softuni.cuisineonline.service.services.util.InputUtil.extractYoutubeVideoId;
import static java.util.stream.Collectors.toList;

@Service
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final UserService userService;
    private final MappingService mappingService;
    private final VideoValidationService validationService;

    public VideoServiceImpl(
            VideoRepository videoRepository,
            UserService userService,
            MappingService mappingService,
            VideoValidationService validationService) {
        this.videoRepository = videoRepository;
        this.userService = userService;
        this.mappingService = mappingService;
        this.validationService = validationService;
    }

    @Override
    public void upload(VideoUploadServiceModel uploadModel) {
        validationService.validateUploadModel(uploadModel);

        String title = uploadModel.getTitle();
        String urlId = extractYoutubeVideoId(uploadModel.getUrl());
        String uploaderUsername = uploadModel.getUploaderUsername();

        Profile uploaderProfile = userService.getUserProfile(uploaderUsername);
        Video video = new Video(title, urlId, uploaderProfile);
        videoRepository.save(video);
    }

    @Override
    public List<VideoServiceModel> getAll() {
        final List<VideoServiceModel> allVideos = videoRepository.findAll().stream()
                .map(v -> {
                    VideoServiceModel serviceModel = mappingService.map(v, VideoServiceModel.class);
                    String username = getUploaderUsername(v);
                    serviceModel.setUploaderUsername(username);
                    return serviceModel;
                }).collect(toList());

        return allVideos;
    }

    @Override
    public VideoServiceModel getById(String id) {
        Video video = getVideoById(id);
        VideoServiceModel serviceModel = mappingService.map(video, VideoServiceModel.class);
        serviceModel.setUploaderUsername(getUploaderUsername(video));
        return serviceModel;
    }

    @Override
    public void edit(VideoEditServiceModel editModel) {
        validationService.validateEditModel(editModel);

        String videoId = editModel.getId();
        Video video = getVideoById(videoId);

        String newTitle = editModel.getTitle();
        String newUrl = extractYoutubeVideoId(editModel.getUrl());
        video.setTitle(newTitle);
        video.setUrl(newUrl);
        videoRepository.save(video);
    }

    @Override
    public void delete(String id) {
        Video video = getVideoById(id);
        videoRepository.delete(video);
    }

    private Video getVideoById(String id) {
        return videoRepository.findById(id).orElseThrow(() ->
                new MissingEntityException("No video found in the database."));
    }

    private String getUploaderUsername(Video video) {
        return video.getUploader().getUser().getUsername();
    }
}
