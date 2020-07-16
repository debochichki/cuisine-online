package com.softuni.cuisineonline.data.repositories;

import com.softuni.cuisineonline.data.models.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, String> {
}
