package com.softuni.cuisineonline.web.interceptors;

import com.softuni.cuisineonline.service.services.domain.UserService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.atomic.AtomicReference;

public class UserRankInterceptor extends HandlerInterceptorAdapter {

    private final UserService userService;
    private AtomicReference<String> uploaderUsername = new AtomicReference<>();

    public UserRankInterceptor(
            UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Get uploader username before POST request execution
        if (request.getMethod().equals("POST")) {
            String username = request.getParameterMap().get("uploaderUsername")[0];
            uploaderUsername.set(username);
        }

        return true;
    }

    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView) {
        // Update User Rank only after successful Upload or Delete POST request
        if (request.getMethod().equals("POST")) {
            String username = uploaderUsername.get();
            userService.updateUserRank(username);
        }
    }
}
