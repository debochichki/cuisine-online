package com.softuni.cuisineonline.service.services.util;

import java.lang.annotation.Annotation;

public final class AnnotationUtil {

    private AnnotationUtil() {
        // Prevent instantiation
    }

    public static <A extends Annotation> boolean isAnnotated(Object src, Class<A> annotationClass) {
        return src.getClass().getAnnotationsByType(annotationClass).length > 0;
    }
}
