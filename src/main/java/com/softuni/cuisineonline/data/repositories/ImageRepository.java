package com.softuni.cuisineonline.data.repositories;

import com.softuni.cuisineonline.data.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {
}
