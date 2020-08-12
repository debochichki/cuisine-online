package com.softuni.cuisineonline.service.services.domain;

import com.softuni.cuisineonline.data.models.Rank;
import com.softuni.cuisineonline.data.models.Recipe;
import com.softuni.cuisineonline.data.models.Role;
import com.softuni.cuisineonline.data.models.User;
import com.softuni.cuisineonline.data.repositories.RoleRepository;
import com.softuni.cuisineonline.data.repositories.UserRepository;
import com.softuni.cuisineonline.errors.ValidationException;
import com.softuni.cuisineonline.service.base.TestServiceBase;
import com.softuni.cuisineonline.service.models.user.RoleStanding;
import com.softuni.cuisineonline.service.models.user.UserProfileServiceModel;
import com.softuni.cuisineonline.service.models.user.UserServiceModel;
import com.softuni.cuisineonline.util.TestUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.softuni.cuisineonline.service.services.util.Constants.DEFAULT_EMPTY_FIELD_VALUE;
import static java.util.stream.Collectors.toList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

class UserServiceIT extends TestServiceBase {

    @Autowired
    UserService service;

    @MockBean
    UserRepository userRepository;

    @MockBean
    RoleRepository roleRepository;

    private final List<User> users = TestUtils.getUsers();

    @Override
    protected void beforeEach() {
        super.beforeEach();
    }

    @Test
    void getUserProfile_WhenValidUsername_ShouldReturnCorrectServiceModel() {
        User user = TestUtils.getRandomListValue(users);
        Mockito.when(userRepository.findByUsername(eq(user.getUsername())))
                .thenReturn(Optional.of(user));

        UserProfileServiceModel userProfile = service.getUserProfile(user.getUsername());

        Assert.assertEquals(user.getUsername(), userProfile.getUsername());
        Assert.assertEquals(user.getEmail(), userProfile.getEmail());
        Assert.assertEquals(user.getProfile().getRank().toString(), userProfile.getRank());
        Assert.assertEquals(user.getProfile().getRecipes().size(), userProfile.getRecipesCount());
        Assert.assertEquals(Objects.requireNonNullElse(user.getTelephoneNumber(), DEFAULT_EMPTY_FIELD_VALUE),
                userProfile.getTelephoneNumber());
    }

    @Test
    void getAllUsers_ShouldReturnTheCorrectServiceModelsSorted() {
        Mockito.when(userRepository.findAll())
                .thenReturn(users);

        List<UserServiceModel> actualUsers = service.getAllUsers();

        Assert.assertEquals(users.size(), actualUsers.size());
        List<User> expectedUsers = new ArrayList<>(users);
        expectedUsers.sort((f, s) -> {
            int result = s.getAuthorities().size() - f.getAuthorities().size();
            if (result == 0) {
                result = f.getUsername().compareTo(s.getUsername());
            }
            return result;
        });

        for (int i = 0; i < expectedUsers.size(); i++) {

            User expected = expectedUsers.get(i);
            UserServiceModel actual = actualUsers.get(i);
            Assert.assertEquals(expected.getUsername(), actual.getUsername());
            Assert.assertEquals(expected.getEmail(), actual.getEmail());
            Assert.assertEquals(
                    RoleStanding.resolve(expected.getAuthorities()
                            .stream()
                            .map(GrantedAuthority::getAuthority).collect(toList())), actual.getRoleStanding());
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    void deleteInactiveUsers_WhenUserIsNOTRootAndHasNotLoggedInForTheDeclaredPeriod_ShouldDeleteUser() {
        int periodInMoths = TestUtils.getRandomListValue(List.of(1, 3, 6, 9));
        List<User> regularUsers = users.stream().filter(u -> !isRoot(u)).collect(toList());
        User user = TestUtils.getRandomListValue(regularUsers);
        LocalDate lastLoginDate =
                LocalDate.now().minus(periodInMoths + 1, ChronoUnit.MONTHS);
        user.setLastLogin(lastLoginDate);
        Mockito.when(userRepository.findAll())
                .thenReturn(users);

        service.deleteInactiveUsers(periodInMoths);

        ArgumentCaptor<List<User>> argument = ArgumentCaptor.forClass(List.class);
        verify(userRepository).deleteAll(argument.capture());
        List<User> deletedUsers = argument.getValue();

        Assert.assertEquals(1, deletedUsers.size());
        Assert.assertEquals(user, deletedUsers.get(0));
    }

    @Test
    @SuppressWarnings("unchecked")
    void deleteInactiveUsers_WhenUserISRootAndHasNotLoggedInForTheDeclaredPeriod_ShouldNotDeleteUser() {
        int periodInMoths = TestUtils.getRandomListValue(List.of(1, 3, 6, 9));
        User root = users.stream().filter(this::isRoot).findFirst().orElseThrow();
        LocalDate lastLoginDate =
                LocalDate.now().minus(periodInMoths + 1, ChronoUnit.MONTHS);
        root.setLastLogin(lastLoginDate);
        Mockito.when(userRepository.findAll()).thenReturn(users);

        service.deleteInactiveUsers(periodInMoths);

        ArgumentCaptor<List<User>> argument = ArgumentCaptor.forClass(List.class);
        verify(userRepository).deleteAll(argument.capture());
        List<User> deletedUsers = argument.getValue();

        Assert.assertEquals(0, deletedUsers.size());
    }

    @Test
    void updateUserLoginDate_ShouldSetUserLoginDateToCurrent() {
        int periodInMoths = TestUtils.getRandomListValue(List.of(1, 3, 6, 9));
        User user = TestUtils.getRandomListValue(users);
        LocalDate lastLoginDate =
                LocalDate.now().minus(periodInMoths, ChronoUnit.MONTHS);
        user.setLastLogin(lastLoginDate);
        Mockito.when(userRepository.findByUsername(eq(user.getUsername())))
                .thenReturn(Optional.of(user));

        service.updateUserLoginDate(user.getUsername());

        Assert.assertEquals(LocalDate.now(), user.getLastLogin());
    }

    @Test
    void updateUserRank_ShouldUpdateToCorrespondingRankBasedOnTheUploadedRecipesCount() {
        User user = TestUtils.getRandomListValue(users);
        List<Recipe> uploadedRecipes = new ArrayList<>();
        for (int i = 0; i < Rank.ENTHUSIAST.getRecipesCount(); i++) {
            uploadedRecipes.add(new Recipe());
        }

        user.getProfile().setRecipes(uploadedRecipes);
        Mockito.when(userRepository.findByUsername(eq(user.getUsername())))
                .thenReturn(Optional.of(user));

        service.updateUserRank(user.getUsername());
        Assert.assertEquals(Rank.ENTHUSIAST, user.getProfile().getRank());

        user.getProfile().getRecipes().clear();
        service.updateUserRank(user.getUsername());
        Assert.assertEquals(Rank.NOVICE, user.getProfile().getRank());
    }

    @Test
    void promoteToAdmin_WhenUser_ShouldPromoteToAdmin() {
        User user = users.stream().filter(this::isUser).findFirst().orElseThrow();
        Mockito.when(userRepository.findById(eq(user.getId())))
                .thenReturn(Optional.of(user));
        Mockito.when(roleRepository.findByAuthority(eq("ADMIN")))
                .thenReturn(getRoleOpt("ADMIN"));

        service.promoteToAdmin(user.getId());

        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(argument.capture());
        User promotedUser = argument.getValue();

        Assert.assertEquals(user.getUsername(), promotedUser.getUsername());
        Assert.assertTrue(isAdmin(user));
    }

    @Test
    void promoteToAdmin_WhenUserHasAuthorityAdmin_ShouldThrow() {
        User user = users.stream().filter(this::isUser).findAny().orElseThrow();
        Mockito.when(userRepository.findById(eq(user.getId())))
                .thenReturn(Optional.of(user));
        Mockito.when(roleRepository.findByAuthority(eq("ADMIN")))
                .thenReturn(getRoleOpt("ADMIN"));

        service.promoteToAdmin(user.getId());

        Assert.assertThrows(ValidationException.class, () ->
                service.promoteToAdmin(user.getId()));
    }

    @Test
    void demoteToUser_WhenUserHasAuthorityAdminAndIsNOTRoot_ShouldDemoteToUser() {
        User user = users.stream().filter(this::isAdmin).findAny().orElseThrow();
        Mockito.when(userRepository.findById(eq(user.getId())))
                .thenReturn(Optional.of(user));

        Mockito.when(roleRepository.findByAuthority(eq("ADMIN")))
                .thenReturn(getRoleOpt("ADMIN"));

        service.demoteToUser(user.getId());

        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(argument.capture());
        User demotedUser = argument.getValue();

        Assert.assertEquals(user.getUsername(), demotedUser.getUsername());
        Assert.assertTrue(isUser(user));
    }

    @Test
    void demoteToUser_WhenUserISRoot_ShouldThrow() {
        User user = users.stream().filter(this::isRoot).findAny().orElseThrow();
        Mockito.when(userRepository.findById(eq(user.getId())))
                .thenReturn(Optional.of(user));

        Assert.assertThrows(ValidationException.class, () ->
                service.demoteToUser(user.getId()));
    }

    @Test
    void canModify_WhenPrincipalIsUploader_ShouldReturnTrue() {
        User principal = TestUtils.getRandomListValue(users);
        String uploaderUsername = principal.getUsername();

        Assert.assertTrue(service.canModify(principal.getUsername(), uploaderUsername));
    }

    @Test
    void canModify_WhenPrincipalHasGreaterAuthority_ShouldReturnTrue() {
        User root = users.stream().filter(this::isRoot).findFirst().orElseThrow();
        Mockito.when(userRepository.findByUsername(eq(root.getUsername())))
                .thenReturn(Optional.of(root));

        User admin = users.stream().filter(this::isAdmin).findFirst().orElseThrow();
        Mockito.when(userRepository.findByUsername(eq(admin.getUsername())))
                .thenReturn(Optional.of(admin));

        User user = users.stream().filter(this::isUser).findFirst().orElseThrow();
        Mockito.when(userRepository.findByUsername(eq(user.getUsername())))
                .thenReturn(Optional.of(user));

        Assert.assertTrue(service.canModify(root.getUsername(), admin.getUsername()));
        Assert.assertTrue(service.canModify(admin.getUsername(), user.getUsername()));
    }

    @Test
    void canModify_WhenPrincipalHasLesserAuthority_ShouldReturnFalse() {
        User root = users.stream().filter(this::isRoot).findFirst().orElseThrow();
        Mockito.when(userRepository.findByUsername(eq(root.getUsername())))
                .thenReturn(Optional.of(root));

        User admin = users.stream().filter(this::isAdmin).findFirst().orElseThrow();
        Mockito.when(userRepository.findByUsername(eq(admin.getUsername())))
                .thenReturn(Optional.of(admin));

        User user = users.stream().filter(this::isUser).findFirst().orElseThrow();
        Mockito.when(userRepository.findByUsername(eq(user.getUsername())))
                .thenReturn(Optional.of(user));

        Assert.assertFalse(service.canModify(admin.getUsername(), root.getUsername()));
        Assert.assertFalse(service.canModify(user.getUsername(), admin.getUsername()));
    }

    private boolean isRoot(User u) {
        return u.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(toList())
                .contains("ROOT");
    }

    private boolean isAdmin(User u) {
        List<String> userAuthorities = u.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(toList());
        return !userAuthorities.contains("ROOT") && userAuthorities.contains("ADMIN");
    }

    private boolean isUser(User u) {
        List<String> userAuthorities = u.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(toList());
        return userAuthorities.size() == 1 && userAuthorities.get(0).equals("USER");
    }

    private Optional<Role> getRoleOpt(String authority) {
        return Optional.of(new Role(authority));
    }
}