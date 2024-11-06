package dev.patrick.mealmaker.recipe;

import dev.patrick.mealmaker.user.UserDTO;

import java.util.List;

public class RecipeDTO {

    public record RecipeDisplay(
            Integer id,
            String title,
            String description,
            Integer servings,
            Double totalCost,
            Integer prepTime,
            Integer cookTime,
            List<RecipeIngredientDTO.RecipeIngredientDisplay> ingredients,
            String image,
            UserDTO.UserRecipeDisplay user,
            List<Instruction> instructions

    ) {}

    public record RecipeUserDisplay(
            Integer id,
            String title,
            String description,
            Integer servings,
            Double totalCost,
            Integer prepTime,
            Integer cookTime,
            List<RecipeIngredientDTO.RecipeIngredientDisplay> ingredients,
            String image,
            List<Instruction> instructions
    ) {}

}
