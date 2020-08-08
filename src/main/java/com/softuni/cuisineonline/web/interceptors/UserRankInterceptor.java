package com.softuni.cuisineonline.web.interceptors;

import com.softuni.cuisineonline.service.services.domain.AuthenticatedUserFacade;
import com.softuni.cuisineonline.service.services.domain.UserService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserRankInterceptor extends HandlerInterceptorAdapter {

    private final AuthenticatedUserFacade authenticationFacade;
    private final UserService userService;

    public UserRankInterceptor(
            AuthenticatedUserFacade authenticationFacade,
            UserService userService) {
        this.authenticationFacade = authenticationFacade;
        this.userService = userService;
    }

    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView) throws Exception {
        // Update User Rank only after successful post request -> Update or Delete
        if (request.getMethod().equals("POST")) {
            String username = authenticationFacade.getPrincipalName();
            userService.updateUserRank(username);
        }
    }
}
