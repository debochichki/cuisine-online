package com.softuni.cuisineonline.service.services.domain.impl;

import com.softuni.cuisineonline.data.models.Profile;
import com.softuni.cuisineonline.data.models.User;
import com.softuni.cuisineonline.data.repositories.UserRepository;
import com.softuni.cuisineonline.errors.NoSuchUserException;
import com.softuni.cuisineonline.service.services.domain.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Profile getUserProfile(final String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchUserException("No user with username: " + username));

        return user.getProfile();
    }
}
