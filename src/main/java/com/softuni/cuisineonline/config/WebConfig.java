package com.softuni.cuisineonline.config;

import com.softuni.cuisineonline.service.services.domain.AuthenticatedUserFacade;
import com.softuni.cuisineonline.service.services.domain.UserService;
import com.softuni.cuisineonline.web.interceptors.RoleStandingInterceptor;
import com.softuni.cuisineonline.web.interceptors.UserRankInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthenticatedUserFacade authenticationFacade;
    private final UserService userService;

    public WebConfig(AuthenticatedUserFacade authenticationFacade,
                     UserService userService) {
        this.authenticationFacade = authenticationFacade;
        this.userService = userService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(roleStandingInterceptor())
                .addPathPatterns("/users/all");
        registry.addInterceptor(userRankInterceptor())
                .addPathPatterns("/recipes/upload", "/recipes/delete");
    }

    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public RoleStandingInterceptor roleStandingInterceptor() {
        return new RoleStandingInterceptor(authenticationFacade);
    }

    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public UserRankInterceptor userRankInterceptor() {
        return new UserRankInterceptor(userService);
    }

    @Bean
    public RedirectStrategy redirectStrategy() {
        return new DefaultRedirectStrategy();
    }
}
