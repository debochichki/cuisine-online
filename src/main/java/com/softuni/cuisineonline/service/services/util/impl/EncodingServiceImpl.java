package com.softuni.cuisineonline.service.services.util.impl;

import com.softuni.cuisineonline.service.services.util.EncodingService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncodingServiceImpl implements EncodingService {

    private final BCryptPasswordEncoder encoder;

    public EncodingServiceImpl(BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public String encode(String s) {
        return encoder.encode(s);
    }
}
