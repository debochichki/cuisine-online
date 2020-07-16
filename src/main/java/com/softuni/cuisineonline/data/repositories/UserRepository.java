package com.softuni.cuisineonline.data.repositories;

import com.softuni.cuisineonline.data.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
