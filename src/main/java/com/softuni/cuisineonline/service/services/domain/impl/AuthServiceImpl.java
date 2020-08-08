package com.softuni.cuisineonline.service.services.domain.impl;

import com.softuni.cuisineonline.data.models.Profile;
import com.softuni.cuisineonline.data.models.Rank;
import com.softuni.cuisineonline.data.models.Role;
import com.softuni.cuisineonline.data.models.User;
import com.softuni.cuisineonline.data.repositories.RoleRepository;
import com.softuni.cuisineonline.data.repositories.UserRepository;
import com.softuni.cuisineonline.service.models.auth.UserRegisterServiceModel;
import com.softuni.cuisineonline.service.services.domain.AuthService;
import com.softuni.cuisineonline.service.services.util.EncodingService;
import com.softuni.cuisineonline.service.services.util.MappingService;
import com.softuni.cuisineonline.service.services.validation.AuthValidationService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private final EncodingService encodingService;
    private final AuthValidationService validationService;
    private final MappingService mappingService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AuthServiceImpl(
            EncodingService encodingService,
            AuthValidationService validationService,
            MappingService mappingService,
            UserRepository userRepository,
            RoleRepository roleRepository) {
        this.encodingService = encodingService;
        this.validationService = validationService;
        this.mappingService = mappingService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    private void init() {
        if (roleRepository.count() == 0L) {
            final List<Role> authorities =
                    List.of(
                            new Role("ROOT"),
                            new Role("ADMIN"),
                            new Role("USER")
                    );
            roleRepository.saveAll(authorities);
        }
    }

    @Override
    public void register(UserRegisterServiceModel registerModel) {
        validationService.validateRegisterModel(registerModel);

        String plainPassword = registerModel.getPassword();
        String encodedPassword = encodingService.encode(plainPassword);

        User user = mappingService.map(registerModel, User.class);
        user.setPassword(encodedPassword);
        user.setLastLogin(LocalDate.now());
        Profile profile = new Profile();
        profile.setRank(Rank.NOVICE);
        user.setProfile(profile);

        if (userRepository.count() == 0L) {
            // Set all authorities
            user.setAuthorities(new HashSet<>(roleRepository.findAll()));
        } else {
            // Set role USER
            user.setAuthorities(Set.of(getRole("USER")));
        }

        userRepository.save(user);
    }

    private Role getRole(String authority) {
        return roleRepository.findByAuthority(authority)
                .orElseThrow(() ->
                        new EntityNotFoundException("No role with authority: " + authority));
    }
}
