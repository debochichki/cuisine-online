package com.softuni.cuisineonline.service.services.domain.impl;

import com.softuni.cuisineonline.data.models.Profile;
import com.softuni.cuisineonline.data.models.Video;
import com.softuni.cuisineonline.data.repositories.VideoRepository;
import com.softuni.cuisineonline.errors.MissingVideoException;
import com.softuni.cuisineonline.service.models.video.VideoEditServiceModel;
import com.softuni.cuisineonline.service.models.video.VideoServiceModel;
import com.softuni.cuisineonline.service.models.video.VideoUploadServiceModel;
import com.softuni.cuisineonline.service.services.domain.UserService;
import com.softuni.cuisineonline.service.services.domain.VideoService;
import com.softuni.cuisineonline.service.services.util.MappingService;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class VideoServiceImpl implements VideoService {

    private static final String YOU_TUBE_DOMAIN_URL = "https://www.youtube.com/watch?v=";

    private final VideoRepository videoRepository;
    private final UserService userService;
    private final MappingService mappingService;

    public VideoServiceImpl(
            VideoRepository videoRepository,
            UserService userService,
            MappingService mappingService) {
        this.videoRepository = videoRepository;
        this.userService = userService;
        this.mappingService = mappingService;
    }

    @Override
    public void upload(VideoUploadServiceModel uploadModel) {
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
        Video video = videoRepository.findById(id).orElseThrow(() ->
                new MissingVideoException("No video found in the database."));
        VideoServiceModel serviceModel = mappingService.map(video, VideoServiceModel.class);
        serviceModel.setUploaderUsername(getUploaderUsername(video));
        return serviceModel;
    }

    @Override
    public void edit(VideoEditServiceModel editModel) {
        // ToDo: Validate input fields

        String videoId = editModel.getId();
        Video video = videoRepository.findById(videoId).orElseThrow(() ->
                new MissingVideoException("No video found in the database."));

        String newTitle = editModel.getTitle();
        String newUrl = extractYoutubeVideoId(editModel.getUrl());
        video.setTitle(newTitle);
        video.setUrl(newUrl);
        videoRepository.save(video);
    }

    @Override
    public void delete(String id) {
        Video video = videoRepository.findById(id).orElseThrow(() ->
                new MissingVideoException("No video found in the database."));
        videoRepository.delete(video);
    }

    private String getUploaderUsername(Video video) {
        return video.getUploader().getUser().getUsername();
    }

    private String extractYoutubeVideoId(String URL) {
        return URL.replace(YOU_TUBE_DOMAIN_URL, "").trim();
    }
}
