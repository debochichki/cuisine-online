package com.softuni.cuisineonline.service.services.util.impl;

import com.softuni.cuisineonline.service.services.util.EncodingService;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

@Service
public class EncodingServiceImpl implements EncodingService {

    @Override
    public String encode(String s) {

        final String result = IntStream.range(0, s.length())
                .map(i -> s.charAt(i) + i)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return result;
    }
}
