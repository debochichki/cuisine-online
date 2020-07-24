package com.softuni.cuisineonline.web.view.controllers.base;

public abstract class BaseController {

    private static final String REDIRECT_PREFIX = "redirect:";

    protected String redirect(String path) {
        return REDIRECT_PREFIX + path;
    }
}
