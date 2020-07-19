package com.softuni.cuisineonline.service.services.domain.impl;

import com.cloudinary.Cloudinary;
import com.softuni.cuisineonline.data.models.Image;
import com.softuni.cuisineonline.errors.ServerException;
import com.softuni.cuisineonline.service.services.domain.CloudinaryService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static com.cloudinary.Cloudinary.emptyMap;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public Image uploadImage(final MultipartFile multipartFile) {
        String url = null;
        File file = null;
        try {
            file = File.createTempFile("temp-file", multipartFile.getOriginalFilename());
            multipartFile.transferTo(file);
            url = cloudinary.uploader().upload(file, emptyMap()).get("url").toString();
        } catch (IOException e) {
            throw new ServerException("Problem while uploading image. Please try again.", e);
        }

        return new Image(url);
    }
}
