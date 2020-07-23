package com.softuni.cuisineonline.service.services.domain.impl;

import com.cloudinary.Cloudinary;
import com.softuni.cuisineonline.data.models.Image;
import com.softuni.cuisineonline.errors.ServerException;
import com.softuni.cuisineonline.service.services.domain.CloudinaryService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

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
        String publicId = null;
        try {
            file = File.createTempFile("temp-file", multipartFile.getOriginalFilename());
            multipartFile.transferTo(file);
            final Map cloudinaryParams = cloudinary.uploader().upload(file, emptyMap());
            url = cloudinaryParams.get("url").toString();
            publicId = cloudinaryParams.get("public_id").toString();
        } catch (IOException e) {
            throw new ServerException("Problem while uploading image. Please try again.", e);
        }

        Image image = new Image();
        image.setPublicId(publicId);
        image.setUrl(url);
        return image;
    }

    @Override
    public void deleteImage(final String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, emptyMap());
        } catch (IOException e) {
            throw new ServerException("Problem while deleting image.", e);
        }
    }
}
