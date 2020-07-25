package com.softuni.cuisineonline.web.view.controllers;

import com.softuni.cuisineonline.service.services.util.AnnotationUtil;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String DEFAULT_ERROR_VIEW = "error";
    private static final String SERVER_ERROR_GENERAL_MESSAGE =
            "Sorry, something went terribly wrong on the server :/";

    @ExceptionHandler({Throwable.class})
    public ModelAndView handleException(Throwable e) throws Throwable {

        // Ensure annotated exceptions are handled by the framework
        final boolean isDomainException = AnnotationUtil.isAnnotated(e, ResponseStatus.class);
        if (isDomainException) {
            throw e;
        }

        // Safety net for all unexpected application failures
        ModelAndView modelAndView = new ModelAndView(DEFAULT_ERROR_VIEW);
        modelAndView.addObject("message", SERVER_ERROR_GENERAL_MESSAGE);
        return modelAndView;
    }
}
