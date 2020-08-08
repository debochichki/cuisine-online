package com.softuni.cuisineonline.web.interceptors;

import com.softuni.cuisineonline.service.services.domain.AuthenticatedUserFacade;
import com.softuni.cuisineonline.service.services.domain.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationSuccessUpdateLoginDateHandler
        implements AuthenticationSuccessHandler {

    private final AuthenticatedUserFacade authenticationFacade;
    private final UserService userService;
    private final RedirectStrategy redirectStrategy;

    public AuthenticationSuccessUpdateLoginDateHandler(
            AuthenticatedUserFacade authenticationFacade,
            UserService userService,
            RedirectStrategy redirectStrategy) {
        this.authenticationFacade = authenticationFacade;
        this.userService = userService;
        this.redirectStrategy = redirectStrategy;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            Authentication authentication) throws IOException {
        String username = authenticationFacade.getPrincipalName();
        userService.updateUserLoginDate(username);
        redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/home");
    }
}
