package com.softuni.cuisineonline.data.repositories;

import com.softuni.cuisineonline.data.models.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, String> {
}
