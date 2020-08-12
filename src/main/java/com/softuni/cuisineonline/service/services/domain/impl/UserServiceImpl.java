package com.softuni.cuisineonline.service.services.domain.impl;

import com.softuni.cuisineonline.data.models.Profile;
import com.softuni.cuisineonline.data.models.Rank;
import com.softuni.cuisineonline.data.models.User;
import com.softuni.cuisineonline.data.repositories.RoleRepository;
import com.softuni.cuisineonline.data.repositories.UserRepository;
import com.softuni.cuisineonline.errors.MissingEntityException;
import com.softuni.cuisineonline.errors.ValidationException;
import com.softuni.cuisineonline.service.models.user.RoleStanding;
import com.softuni.cuisineonline.service.models.user.UserProfileServiceModel;
import com.softuni.cuisineonline.service.models.user.UserServiceModel;
import com.softuni.cuisineonline.service.services.domain.UserService;
import com.softuni.cuisineonline.service.services.util.MappingService;
import com.softuni.cuisineonline.service.services.util.OutputUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import static com.softuni.cuisineonline.service.services.util.Constants.DEFAULT_EMPTY_FIELD_VALUE;
import static java.util.stream.Collectors.toList;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MappingService mappingService;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository,
                           MappingService mappingService,
                           RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.mappingService = mappingService;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserProfileServiceModel getUserProfile(String username) {
        User user = getUserByUsername(username);
        UserProfileServiceModel userDetails = mappingService.map(user, UserProfileServiceModel.class);
        userDetails.setRank(user.getProfile().getRank().toString());
        userDetails.setRecipesCount(user.getProfile().getRecipes().size());
        String telephoneNumber =
                OutputUtil.ifEmptySetDefault(user.getTelephoneNumber(), DEFAULT_EMPTY_FIELD_VALUE);
        userDetails.setTelephoneNumber(telephoneNumber);
        return userDetails;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserByUsername(username);
    }

    @Override
    public List<UserServiceModel> getAllUsers() {
        List<UserServiceModel> serviceModels = userRepository.findAll().stream()
                .map(u -> {
                    UserServiceModel serviceModel = mappingService.map(u, UserServiceModel.class);
                    List<String> authorities = extractAuthorities(u);
                    serviceModel.setRoleStanding(RoleStanding.resolve(authorities));
                    return serviceModel;
                }).sorted(
                        Comparator.comparing(UserServiceModel::getRoleStanding).reversed()
                                .thenComparing(UserServiceModel::getUsername))
                .collect(toList());

        return serviceModels;
    }

    /**
     * Deletes all users that have not logged in for the declared period of time in months
     */
    @Override
    public void deleteInactiveUsers(final int periodInMonths) {
        final Predicate<User> isInactiveForDeletion = (user) ->
                !extractAuthorities(user).contains("ROOT") &&
                        Period.between(user.getLastLogin(), LocalDate.now()).toTotalMonths() - periodInMonths > 0;

        List<User> usersForDeletion =
                userRepository.findAll().stream()
                        .filter(isInactiveForDeletion)
                        .collect(toList());

        userRepository.deleteAll(usersForDeletion);
    }

    @Override
    public void updateUserLoginDate(String username) {
        User user = getUserByUsername(username);
        user.setLastLogin(LocalDate.now());
        userRepository.save(user);
    }

    @Override
    public void updateUserRank(String username) {
        User user = getUserByUsername(username);
        Profile profile = user.getProfile();
        int uploadedRecipesCount = profile.getRecipes().size();
        Rank updatedRank = Rank.resolve(uploadedRecipesCount);
        profile.setRank(updatedRank);
        userRepository.save(user);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void promoteToAdmin(String id) {
        User user = getUserById(id);
        List<String> authorities = extractAuthorities(user);
        if (authorities.contains("ADMIN")) {
            throw new ValidationException("User already has authority: ADMIN");
        }

        Collection<GrantedAuthority> userAuthorities
                = (Collection<GrantedAuthority>) user.getAuthorities();
        userAuthorities.add(getAuthority("ADMIN"));
        userRepository.save(user);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void demoteToUser(String id) {
        User user = getUserById(id);
        List<String> authorities = extractAuthorities(user);
        if (authorities.contains("ROOT")) {
            throw new ValidationException("Cannot demote ROOT user.");
        }

        Collection<GrantedAuthority> userAuthorities
                = (Collection<GrantedAuthority>) user.getAuthorities();
        userAuthorities.remove(getAuthority("ADMIN"));
        userRepository.save(user);
    }

    @Override
    public boolean canModify(String principalName, String uploaderUsername) {
        if (principalName == null || uploaderUsername == null) {
            throw new IllegalArgumentException("Arguments cannot be null.");
        }

        return isOwner(principalName, uploaderUsername) ||
                hasAuthorityToModify(principalName, uploaderUsername);
    }

    private boolean isOwner(String principalName, String uploaderUsername) {
        return principalName.equals(uploaderUsername);
    }

    private boolean hasAuthorityToModify(String principalName, String uploaderUsername) {
        RoleStanding principalStanding =
                RoleStanding.resolve(getUserAuthorities(principalName));
        RoleStanding uploaderStanding =
                RoleStanding.resolve(getUserAuthorities(uploaderUsername));
        return principalStanding.compareTo(uploaderStanding) > 0;
    }

    private User getUserById(final String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new MissingEntityException("No user with id: " + userId));
    }

    private User getUserByUsername(final String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new MissingEntityException("No user with username: " + username));
    }

    private List<String> extractAuthorities(User user) {
        return user.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(toList());
    }

    private List<String> getUserAuthorities(String username) {
        User user = getUserByUsername(username);
        return extractAuthorities(user);
    }

    private GrantedAuthority getAuthority(final String authorityStr) {
        return roleRepository.findByAuthority(authorityStr)
                .orElseThrow(() ->
                        new EntityNotFoundException("No role with authority: " + authorityStr));
    }
}
