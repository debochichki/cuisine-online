package com.softuni.cuisineonline.service.services.domain;

import com.softuni.cuisineonline.data.models.Rank;
import com.softuni.cuisineonline.data.models.User;
import com.softuni.cuisineonline.data.repositories.UserRepository;
import com.softuni.cuisineonline.errors.ValidationException;
import com.softuni.cuisineonline.service.base.TestServiceBase;
import com.softuni.cuisineonline.service.models.auth.UserRegisterServiceModel;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;

import java.util.stream.Collectors;

import static com.softuni.cuisineonline.util.TestUtils.ALL_ROLES_MAP;
import static org.mockito.Mockito.verify;

class AuthServiceIT extends TestServiceBase {

    private static final String VALID_USERNAME = "Koleto";
    private static final String VALID_PASSWORD = "koleto123";
    private static final String VALID_EMAIL_ADDRESS = "koleto@abv.bg";

    private UserRegisterServiceModel registerModel;

    @Autowired
    AuthService service;

    @MockBean
    UserRepository userRepository;

    @Override
    protected void beforeEach() {
        super.beforeEach();
        registerModel = new UserRegisterServiceModel();
        registerModel.setUsername(VALID_USERNAME);
        registerModel.setPassword(VALID_PASSWORD);
        registerModel.setConfirmPassword(VALID_PASSWORD);
        registerModel.setEmail(VALID_EMAIL_ADDRESS);
    }

    @Test
    public void register_WhenValidRegisterModelAndFirstRegisteredUser_ShouldSetAllAuthorities() {
        Mockito.when(userRepository.count()).thenReturn(0L);

        service.register(registerModel);

        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(argument.capture());
        final User registeredUser = argument.getValue();

        Assert.assertTrue(registeredUser.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet())
                .containsAll(ALL_ROLES_MAP.keySet()));
    }

    @Test
    public void register_WhenValidRegisterModelAndSecondRegisteredUser_ShouldSetOnlyUserAuthority() {
        Mockito.when(userRepository.count()).thenReturn(1L);

        service.register(registerModel);

        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(argument.capture());
        final User registeredUser = argument.getValue();

        Assert.assertEquals(1, registeredUser.getAuthorities().size());
        Assert.assertEquals("USER", registeredUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList()).get(0));
    }

    @Test
    public void register_WhenValidRegisterModel_ShouldEncodePassword() {
        Mockito.when(userRepository.count()).thenReturn(1L);

        service.register(registerModel);

        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(argument.capture());
        final User registeredUser = argument.getValue();

        Assert.assertNotEquals(VALID_PASSWORD, registeredUser.getPassword());
    }

    @Test
    public void register_WhenValidRegisterModel_ShouldSetProfileWithCorrectRank() {
        Mockito.when(userRepository.count()).thenReturn(1L);

        service.register(registerModel);

        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(argument.capture());
        final User registeredUser = argument.getValue();

        Assert.assertNotNull(registeredUser.getProfile());
        Assert.assertEquals(Rank.NOVICE, registeredUser.getProfile().getRank());
    }

    @Test
    public void register_WhenRegisterModelWithNullObligatoryField_ShouldThrow() {
        registerModel.setUsername(null);
        Assert.assertThrows(ValidationException.class, () -> service.register(registerModel));
    }

    @Test
    public void register_WhenRegisterModelWithUsernameInvalidLength_ShouldThrow() {
        final String shortUsername = "Hi";
        final String longUsername = "TooLongUsernameToRegister";

        registerModel.setUsername(shortUsername);
        Assert.assertThrows(ValidationException.class, () -> service.register(registerModel));

        registerModel.setUsername(longUsername);
        Assert.assertThrows(ValidationException.class, () -> service.register(registerModel));
    }

    @Test
    public void register_WhenRegisterModelPasswordsNotMatching_ShouldThrow() {
        registerModel.setConfirmPassword(VALID_PASSWORD + "invalid");

        Assert.assertThrows(ValidationException.class, () -> service.register(registerModel));
    }

    @Test
    public void register_WhenRegisterModelWithInvalidEmail_ShouldThrow() {
        final String invalidEmail = VALID_PASSWORD.replace("@", "");
        registerModel.setEmail(invalidEmail);

        Assert.assertThrows(ValidationException.class, () -> service.register(registerModel));
    }

    @Test
    public void register_WhenRegisterModelUsernameIsTaken_ShouldThrow() {
        Mockito.when(userRepository.existsByUsername(VALID_USERNAME)).thenReturn(true);

        Assert.assertThrows(ValidationException.class, () -> service.register(registerModel));
    }

    @Test
    public void register_WhenRegisterModelEmailAddressIsTaken_ShouldThrow() {
        Mockito.when(userRepository.existsByEmail(VALID_EMAIL_ADDRESS)).thenReturn(true);

        Assert.assertThrows(ValidationException.class, () -> service.register(registerModel));
    }
}