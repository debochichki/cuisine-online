package com.softuni.cuisineonline.service.services.domain;

import com.softuni.cuisineonline.service.models.user.UserProfileServiceModel;
import com.softuni.cuisineonline.service.models.user.UserServiceModel;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserProfileServiceModel getUserProfile(String username);

    @Override
    UserDetails loadUserByUsername(String s) throws UsernameNotFoundException;

    List<UserServiceModel> getAllUsers();

    List<String> getUserAuthorities(String username);

    void deleteInactiveUsers(int periodInMonths);

    void updateUserLoginDate(String username);

    void updateUserRank(String username);

    void promoteToAdmin(String id);

    void demoteToUser(String id);
}
