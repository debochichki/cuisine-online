package com.softuni.cuisineonline.web.interceptors;

import com.softuni.cuisineonline.service.models.user.RoleStanding;
import com.softuni.cuisineonline.service.services.domain.AuthenticatedUserFacade;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class RoleStandingInterceptor extends HandlerInterceptorAdapter {

    private final AuthenticatedUserFacade authenticationFacade;

    public RoleStandingInterceptor(AuthenticatedUserFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        List<String> authorities = authenticationFacade.getPrincipalAuthorities();
        if (authorities.isEmpty() || authorities.contains("ROLE_ANONYMOUS")) {
            return false;
        }

        RoleStanding roleStanding = RoleStanding.resolve(authorities);
        HttpSession session = request.getSession();
        session.setAttribute("principalRoleStanding", roleStanding);

        return true;
    }
}
