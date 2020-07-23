package com.softuni.cuisineonline.service.services.validation.impl;

import com.softuni.cuisineonline.errors.ValidationException;
import com.softuni.cuisineonline.service.models.appliance.ApplianceCreateServiceModel;
import com.softuni.cuisineonline.service.services.validation.ApplianceValidationService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.softuni.cuisineonline.service.services.util.Constants.TITLE_LENGTH_LOWER_BOUND;
import static com.softuni.cuisineonline.service.services.util.Constants.TITLE_LENGTH_UPPER_BOUND;
import static com.softuni.cuisineonline.service.services.util.InputUtil.areAllFilled;
import static com.softuni.cuisineonline.service.services.util.InputUtil.isLengthInBounds;

@Service
public class ApplianceValidationServiceImpl implements ApplianceValidationService {


    @Override
    public void validateCreateModel(ApplianceCreateServiceModel createModel) {
        String name = createModel.getName();
        MultipartFile multipartFile = createModel.getImage();

        if (!areAllFilled(name)) {
            throw new ValidationException("Fill all obligatory fields.");
        }

        if (!isLengthInBounds(name, TITLE_LENGTH_LOWER_BOUND, TITLE_LENGTH_UPPER_BOUND)) {
            throw new ValidationException(
                    String.format("Appliance name length needs to be between %d and %d symbols",
                            TITLE_LENGTH_LOWER_BOUND,
                            TITLE_LENGTH_UPPER_BOUND));
        }

        if (multipartFile.isEmpty()) {
            throw new ValidationException("You must specify an image file.");
        }
    }
}
