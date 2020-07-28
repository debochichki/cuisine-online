package com.softuni.cuisineonline.data.repositories;

import com.softuni.cuisineonline.data.models.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, String> {

    List<Recipe> findAllByOrderByTitleAsc();

    List<Recipe> findAllByTypeOrderByTitleAsc(Recipe.Type type);
}
