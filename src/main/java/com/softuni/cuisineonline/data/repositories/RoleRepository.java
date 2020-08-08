package com.softuni.cuisineonline.data.repositories;

import com.softuni.cuisineonline.data.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    Optional<Role> findByAuthority(String authority);
}
