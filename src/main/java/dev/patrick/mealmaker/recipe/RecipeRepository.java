package dev.patrick.mealmaker.recipe;

import org.springframework.data.jpa.repository.JpaRepository;


public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

}
