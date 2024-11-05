package dev.patrick.mealmaker.user;

import dev.patrick.mealmaker.recipe.RecipeDTO;

import java.util.List;

public class UserDTO {

    public record UserDisplay(
            Integer id,
            String firstname,
            String lastname,
            String email,
            Role role,
            List<RecipeDTO.RecipeUserDisplay> recipes
    ) {}

    public record UserRecipeDisplay(
            Integer id,
            String firstname,
            String lastname,
            String email,
            List<Integer> recipeIds
    ) {}

}
