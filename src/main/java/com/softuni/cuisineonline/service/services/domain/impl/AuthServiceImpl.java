package com.softuni.cuisineonline.service.services.domain.impl;

import com.softuni.cuisineonline.data.models.Profile;
import com.softuni.cuisineonline.data.models.Rank;
import com.softuni.cuisineonline.data.models.User;
import com.softuni.cuisineonline.data.repositories.UserRepository;
import com.softuni.cuisineonline.errors.ValidationException;
import com.softuni.cuisineonline.service.models.auth.UserLoginServiceModel;
import com.softuni.cuisineonline.service.models.auth.UserRegisterServiceModel;
import com.softuni.cuisineonline.service.services.domain.AuthService;
import com.softuni.cuisineonline.service.services.util.EncodingService;
import com.softuni.cuisineonline.service.services.util.MappingService;
import com.softuni.cuisineonline.service.services.validation.AuthValidationService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final EncodingService encodingService;
    private final AuthValidationService validationService;
    private final MappingService mappingService;
    private final UserRepository userRepository;

    public AuthServiceImpl(
            EncodingService encodingService,
            AuthValidationService validationService,
            MappingService mappingService,
            UserRepository userRepository) {
        this.encodingService = encodingService;
        this.validationService = validationService;
        this.mappingService = mappingService;
        this.userRepository = userRepository;
    }

    @Override
    public void register(UserRegisterServiceModel registerModel) {
        validationService.validateRegisterModel(registerModel);

        String plainPassword = registerModel.getPassword();
        String encodedPassword = encodingService.encode(plainPassword);

        User user = mappingService.map(registerModel, User.class);
        user.setPassword(encodedPassword);
        Profile profile = new Profile();
        profile.setRank(Rank.NOVICE);
        user.setProfile(profile);

        userRepository.save(user);
    }

    @Override
    public String login(UserLoginServiceModel loginModel) {
        String username = loginModel.getUsername();
        String encodedPassword = encodingService.encode(loginModel.getPassword());

        return userRepository.findByUsernameAndPassword(username, encodedPassword)
                .map(User::getUsername)
                .orElseThrow(() -> new ValidationException("Invalid username or password."));
    }
}
