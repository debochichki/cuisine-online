package com.softuni.cuisineonline.service.services.validation.impl;

import com.softuni.cuisineonline.data.repositories.UserRepository;
import com.softuni.cuisineonline.errors.ValidationException;
import com.softuni.cuisineonline.service.models.auth.UserRegisterServiceModel;
import com.softuni.cuisineonline.service.services.util.EncodingService;
import com.softuni.cuisineonline.service.services.validation.AuthValidationService;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthValidationServiceImpl implements AuthValidationService {

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private final UserRepository userRepository;
    private final EncodingService encodingService;

    public AuthValidationServiceImpl(UserRepository userRepository, EncodingService encodingService) {
        this.userRepository = userRepository;
        this.encodingService = encodingService;
    }

    @Override
    public void validateRegisterModel(UserRegisterServiceModel registerModel) {
        String username = registerModel.getUsername();
        String password = registerModel.getPassword();
        String confirmPassword = registerModel.getConfirmPassword();
        String email = registerModel.getEmail();

        if (!areAllFilled(username, password, confirmPassword, email)) {
            throw new ValidationException("Fill all obligatory fields.");
        }

        if (!arePasswordsMatching(password, confirmPassword)) {
            throw new ValidationException("Passwords do not match.");
        }

        if (!isValidEmail(email)) {
            throw new ValidationException("Not a valid email address.");
        }

        if (!isUsernameFree(username)) {
            throw new ValidationException("Username is already taken.");
        }

        if (!isEmailAddressFree(email)) {
            throw new ValidationException("There is already a registered user with this email address.");
        }
    }

    private boolean isUsernameFree(String username) {
        return !userRepository.existsByUsername(username);
    }

    private static boolean isValidEmail(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }

    private boolean isEmailAddressFree(String email) {
        return !userRepository.existsByEmail(email);
    }

    private boolean arePasswordsMatching(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    private boolean areAllFilled(String... obligatoryFields) {
        return Arrays.stream(obligatoryFields).noneMatch(StringUtils::isEmptyOrWhitespace);
    }
}
