package com.softuni.cuisineonline.service.services.validation.impl;

import com.softuni.cuisineonline.data.repositories.UserRepository;
import com.softuni.cuisineonline.errors.ServerException;
import com.softuni.cuisineonline.errors.ValidationException;
import com.softuni.cuisineonline.service.models.video.VideoEditServiceModel;
import com.softuni.cuisineonline.service.models.video.VideoUploadServiceModel;
import com.softuni.cuisineonline.service.services.util.Constants;
import com.softuni.cuisineonline.service.services.validation.VideoValidationService;
import org.springframework.stereotype.Service;

import static com.softuni.cuisineonline.service.services.util.Constants.TITLE_LENGTH_LOWER_BOUND;
import static com.softuni.cuisineonline.service.services.util.Constants.TITLE_LENGTH_UPPER_BOUND;
import static com.softuni.cuisineonline.service.services.util.InputUtil.areAllFilled;
import static com.softuni.cuisineonline.service.services.util.InputUtil.isLengthInBounds;

@Service
public class VideoValidationServiceImpl implements VideoValidationService {

    private final UserRepository userRepository;

    public VideoValidationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void validateUploadModel(VideoUploadServiceModel uploadModel) {
        String url = uploadModel.getUrl();
        String title = uploadModel.getTitle();
        String uploaderUsername = uploadModel.getUploaderUsername();

        validateParameters(url, title);

        if (uploaderUsername == null || uploaderUsername.isBlank()) {
            throw new ServerException("Video upload is initiated by non-logged user.");
        }

        if (!userRepository.existsByUsername(uploaderUsername)) {
            throw new ServerException(
                    "No user with username: " + uploaderUsername + " exists in the database.");
        }
    }

    @Override
    public void validateEditModel(VideoEditServiceModel editModel) {
        String url = editModel.getUrl();
        String title = editModel.getTitle();

        validateParameters(url, title);
    }

    private void validateParameters(String url, String title) {
        if (!areAllFilled(url, title)) {
            throw new ValidationException("Fill all obligatory fields.");
        }

        if (!isLengthInBounds(title, TITLE_LENGTH_LOWER_BOUND, TITLE_LENGTH_UPPER_BOUND)) {
            throw new ValidationException(
                    String.format("Title length needs to be between %d and %d symbols",
                            TITLE_LENGTH_LOWER_BOUND,
                            TITLE_LENGTH_UPPER_BOUND));
        }

        if (!url.startsWith(Constants.YOU_TUBE_DOMAIN_URL)) {
            throw new ValidationException("Not a valid youtube video URL.");
        }


    }
}
