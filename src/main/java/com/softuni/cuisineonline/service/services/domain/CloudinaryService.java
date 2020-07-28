package com.softuni.cuisineonline.service.services.domain;

import com.softuni.cuisineonline.data.models.Image;
import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {

    /**
     * Stores the image on the cloud and returns a domain Image entity
     *
     * @param image
     * @return
     */
    Image uploadImage(MultipartFile image);

    void deleteImage(String publicId);
}
