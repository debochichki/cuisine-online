package com.softuni.cuisineonline.service.services.domain.impl;

import com.softuni.cuisineonline.data.models.Profile;
import com.softuni.cuisineonline.data.models.User;
import com.softuni.cuisineonline.data.repositories.UserRepository;
import com.softuni.cuisineonline.errors.MissingEntityException;
import com.softuni.cuisineonline.service.models.user.UserServiceModel;
import com.softuni.cuisineonline.service.services.domain.UserService;
import com.softuni.cuisineonline.service.services.util.MappingService;
import com.softuni.cuisineonline.service.services.util.OutputUtil;
import org.springframework.stereotype.Service;

import static com.softuni.cuisineonline.service.services.util.Constants.DEFAULT_EMPTY_FIELD_VALUE;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MappingService mappingService;

    public UserServiceImpl(UserRepository userRepository,
                           MappingService mappingService) {
        this.userRepository = userRepository;
        this.mappingService = mappingService;
    }

    @Override
    public Profile getUserProfile(final String username) {
        User user = getUserByUsername(username);
        return user.getProfile();
    }

    @Override
    public UserServiceModel getUserDetails(String username) {
        User user = getUserByUsername(username);
        UserServiceModel userDetails = mappingService.map(user, UserServiceModel.class);
        userDetails.setRank(user.getProfile().getRank().toString());
        userDetails.setRecipesCount(user.getProfile().getRecipes().size());
        String telephoneNumber =
                OutputUtil.ifEmptySetDefault(user.getTelephoneNumber(), DEFAULT_EMPTY_FIELD_VALUE);
        userDetails.setTelephoneNumber(telephoneNumber);
        return userDetails;
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new MissingEntityException("No user with username: " + username));
    }
}
