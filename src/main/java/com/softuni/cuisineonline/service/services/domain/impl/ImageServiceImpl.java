package com.softuni.cuisineonline.service.services.domain.impl;

import com.softuni.cuisineonline.data.repositories.ImageRepository;
import com.softuni.cuisineonline.service.services.domain.ImageService;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public void deleteById(String id) {
        imageRepository.deleteById(id);
    }
}
