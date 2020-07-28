package com.softuni.cuisineonline.data.repositories;

import com.softuni.cuisineonline.data.models.Appliance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplianceRepository extends JpaRepository<Appliance, String> {

    List<Appliance> findAllByIdIn(List<String> ids);
}
