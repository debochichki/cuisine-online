package com.softuni.cuisineonline.data.repositories;

import com.softuni.cuisineonline.data.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {

    List<Comment> findAllByOrderByInstantAsc();
}
