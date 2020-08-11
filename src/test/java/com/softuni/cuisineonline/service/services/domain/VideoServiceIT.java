package com.softuni.cuisineonline.service.services.domain;

import com.softuni.cuisineonline.data.models.User;
import com.softuni.cuisineonline.data.models.Video;
import com.softuni.cuisineonline.data.repositories.UserRepository;
import com.softuni.cuisineonline.data.repositories.VideoRepository;
import com.softuni.cuisineonline.errors.ValidationException;
import com.softuni.cuisineonline.service.base.TestServiceBase;
import com.softuni.cuisineonline.service.models.video.VideoEditServiceModel;
import com.softuni.cuisineonline.service.models.video.VideoServiceModel;
import com.softuni.cuisineonline.service.models.video.VideoUploadServiceModel;
import com.softuni.cuisineonline.util.TestUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.softuni.cuisineonline.service.services.util.Constants.TITLE_LENGTH_UPPER_BOUND;
import static com.softuni.cuisineonline.service.services.util.Constants.YOU_TUBE_DOMAIN_URL;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

class VideoServiceIT extends TestServiceBase {

    private static final int VALID_TITLE_LENGTH = 20;
    private static final String INVALID_LONG_TITLE =
            TestUtils.getRandomString(TITLE_LENGTH_UPPER_BOUND + 1);
    private static final String INVALID_SHORT_TITLE = "T";

    @Autowired
    VideoService service;

    @MockBean
    VideoRepository videoRepository;

    @MockBean
    UserRepository userRepository;

    private final List<User> users = TestUtils.getUsers();
    private final List<Video> videos = new ArrayList<>();

    @Override
    protected void beforeEach() {
        super.beforeEach();
        initVideos();
    }

    private void initVideos() {
        for (User user : users) {
            Video video = new Video();
            video.setId(TestUtils.getRandomString(5));
            video.setUploader(user.getProfile());
            video.setTitle(TestUtils.getRandomString(VALID_TITLE_LENGTH));
            video.setUrl(TestUtils.getRandomString(5));

            videos.add(video);
        }
    }

    @Test
    void upload_WhenValidUploadModel_ShouldSetCorrectValues() {
        User user = TestUtils.getRandomListValue(users);
        VideoUploadServiceModel uploadModel = generateUploadModel(user);

        Mockito.when(userRepository.findByUsername(eq(user.getUsername())))
                .thenReturn(Optional.of(user));
        Mockito.when(userRepository.existsByUsername(eq(user.getUsername())))
                .thenReturn(Boolean.TRUE);

        service.upload(uploadModel);

        ArgumentCaptor<Video> argument = ArgumentCaptor.forClass(Video.class);
        verify(videoRepository).save(argument.capture());
        Video uploadedVideo = argument.getValue();

        Assert.assertEquals(uploadModel.getTitle(), uploadedVideo.getTitle());
        Assert.assertEquals(uploadModel.getUploaderUsername(), uploadedVideo.getUploader().getUser().getUsername());
        Assert.assertEquals(uploadModel.getUrl(), YOU_TUBE_DOMAIN_URL + uploadedVideo.getUrl());
    }

    @Test
    void upload_WhenUploadModelWithNullObligatoryField_ShouldThrow() {
        User user = TestUtils.getRandomListValue(users);
        VideoUploadServiceModel uploadModel = generateUploadModel(user);
        uploadModel.setTitle(null);
        Assert.assertThrows(ValidationException.class, () -> service.upload(uploadModel));
    }

    @Test
    void upload_WhenUploadModelWithInvalidTitleLength_ShouldThrow() {
        User user = TestUtils.getRandomListValue(users);
        VideoUploadServiceModel uploadModel = generateUploadModel(user);

        uploadModel.setTitle(INVALID_SHORT_TITLE);
        Assert.assertThrows(ValidationException.class, () -> service.upload(uploadModel));

        uploadModel.setTitle(INVALID_LONG_TITLE);
        Assert.assertThrows(ValidationException.class, () -> service.upload(uploadModel));
    }

    @Test
    void upload_WhenUploadModelWithInvalidYouTubeUrl_ShouldThrow() {
        User user = TestUtils.getRandomListValue(users);
        VideoUploadServiceModel uploadModel = generateUploadModel(user);

        uploadModel.setUrl(TestUtils.getRandomString(10));
        Assert.assertThrows(ValidationException.class, () -> service.upload(uploadModel));
    }

    @Test
    void getAll_ShouldReturnAllVideos() {
        Mockito.when(videoRepository.findAll())
                .thenReturn(videos);

        List<VideoServiceModel> actualVideos = service.getAll();
        actualVideos.sort(Comparator.comparing(VideoServiceModel::getId));
        videos.sort(Comparator.comparing(Video::getId));

        for (int i = 0; i < videos.size(); i++) {
            Video expectedVideo = videos.get(i);
            VideoServiceModel actualVideo = actualVideos.get(i);
            assertEqualsVideos(expectedVideo, actualVideo);
        }
    }

    @Test
    void getById_WhenValidId_ShouldReturnCorrectServiceModel() {
        for (Video video : videos) {
            String videoId = video.getId();

            Mockito.when(videoRepository.findById(eq(videoId)))
                    .thenReturn(Optional.of(video));

            VideoServiceModel actualVideo = service.getById(videoId);

            assertEqualsVideos(video, actualVideo);
        }
    }

    @Test
    void edit_WhenValidVideoModel_ShouldSetCorrectValues() {
        Video video = TestUtils.getRandomListValue(videos);
        VideoEditServiceModel editModel = generateEditModel(video);

        Mockito.when(videoRepository.findById(video.getId()))
                .thenReturn(Optional.of(video));

        service.edit(editModel);

        ArgumentCaptor<Video> argument = ArgumentCaptor.forClass(Video.class);
        verify(videoRepository).save(argument.capture());
        Video editedVideo = argument.getValue();

        Assert.assertEquals(editModel.getId(), editedVideo.getId());
        Assert.assertEquals(editModel.getTitle(), editedVideo.getTitle());
        Assert.assertEquals(editModel.getUrl(), YOU_TUBE_DOMAIN_URL + editedVideo.getUrl());
    }

    @Test
    void edit_WhenEditModelWithNullObligatoryField_ShouldThrow() {
        Video video = TestUtils.getRandomListValue(videos);
        VideoEditServiceModel editModel = generateEditModel(video);

        editModel.setUrl(null);
        Assert.assertThrows(ValidationException.class, () -> service.edit(editModel));
    }

    @Test
    void edit_WhenEditModelWithInvalidTitleLength_ShouldThrow() {
        Video video = TestUtils.getRandomListValue(videos);
        VideoEditServiceModel editModel = generateEditModel(video);

        editModel.setTitle(INVALID_SHORT_TITLE);
        Assert.assertThrows(ValidationException.class, () -> service.edit(editModel));

        editModel.setTitle(INVALID_LONG_TITLE);
        Assert.assertThrows(ValidationException.class, () -> service.edit(editModel));
    }

    @Test
    void edit_WhenUploadModelWithInvalidYouTubeUrl_ShouldThrow() {
        Video video = TestUtils.getRandomListValue(videos);
        VideoEditServiceModel editModel = generateEditModel(video);

        editModel.setUrl(TestUtils.getRandomString(10));
        Assert.assertThrows(ValidationException.class, () -> service.edit(editModel));
    }

    @Test
    void delete_ShouldRemoveTheCorrectVideo() {
        Video video = TestUtils.getRandomListValue(videos);
        Mockito.when(videoRepository.findById(eq(video.getId())))
                .thenReturn(Optional.of(video));

        service.delete(video.getId());

        verify(videoRepository).delete(video);
    }

    private void assertEqualsVideos(Video expectedVideo, VideoServiceModel actualVideo) {
        Assert.assertEquals(expectedVideo.getId(), actualVideo.getId());
        Assert.assertEquals(expectedVideo.getTitle(), actualVideo.getTitle());
        Assert.assertEquals(
                expectedVideo.getUploader().getUser().getUsername(), actualVideo.getUploaderUsername());
        Assert.assertEquals(expectedVideo.getUrl(), actualVideo.getUrl());
    }

    private VideoEditServiceModel generateEditModel(Video video) {
        VideoEditServiceModel editModel = new VideoEditServiceModel();
        editModel.setId(video.getId());
        editModel.setTitle(TestUtils.getRandomString(VALID_TITLE_LENGTH));
        editModel.setUrl(YOU_TUBE_DOMAIN_URL + TestUtils.getRandomString(5));
        return editModel;
    }

    private VideoUploadServiceModel generateUploadModel(User user) {
        VideoUploadServiceModel uploadModel = new VideoUploadServiceModel();
        uploadModel.setTitle(TestUtils.getRandomString(VALID_TITLE_LENGTH));
        uploadModel.setUrl(YOU_TUBE_DOMAIN_URL + TestUtils.getRandomString(5));
        uploadModel.setUploaderUsername(user.getUsername());
        return uploadModel;
    }
}