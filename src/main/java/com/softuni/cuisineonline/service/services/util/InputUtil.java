package com.softuni.cuisineonline.service.services.util;

import org.thymeleaf.util.StringUtils;

import java.util.Arrays;

import static com.softuni.cuisineonline.service.services.util.Constants.YOU_TUBE_DOMAIN_URL;

public final class InputUtil {

    private InputUtil() {
        // Prevent instantiation
    }

    public static boolean isLengthInBounds(String s, int lowerBound, int upperBound) {
        int length = s.trim().length();
        if (lowerBound < 1 || upperBound < 1) {
            throw new IllegalArgumentException("Bounds values must be positive.");
        }

        if (lowerBound >= upperBound) {
            throw new IllegalArgumentException("Upper bound must be greater than the lower.");
        }

        return length >= lowerBound && length <= upperBound;
    }

    public static boolean areAllFilled(String... obligatoryFields) {
        if (obligatoryFields == null || obligatoryFields.length == 0) {
            throw new IllegalArgumentException("Argument cannot be null or empty.");
        }

        return Arrays.stream(obligatoryFields).noneMatch(StringUtils::isEmptyOrWhitespace);
    }

    public static String extractYoutubeVideoId(String URL) {
        return URL.replace(YOU_TUBE_DOMAIN_URL, "").trim();
    }

    public static String[][] parseData(String data, String lineDelimiter, String valueDelimiter) {
        String[] lines = data.split(lineDelimiter);
        String[][] result = new String[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            result[i] = lines[i].split(valueDelimiter);
        }

        return result;
    }
}
