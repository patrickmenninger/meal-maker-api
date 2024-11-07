package dev.patrick.mealmaker.user;

import dev.patrick.mealmaker.recipe.Recipe;
import dev.patrick.mealmaker.recipe.RecipeDTO;
import dev.patrick.mealmaker.recipe.ingredient.RecipeIngredient;
import dev.patrick.mealmaker.recipe.ingredient.RecipeIngredientDTO;

import java.util.List;
import java.util.stream.Collectors;

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

    public static UserDTO.UserDisplay convertToUserDTO(User user) {

        List<RecipeDTO.RecipeUserDisplay> recipes = user.getRecipes().stream()
                .map(UserDTO::convertToRecipeUserDTO)
                .collect(Collectors.toList());

        return new UserDTO.UserDisplay(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getRole(),
                recipes
        );
    }

    public static RecipeDTO.RecipeUserDisplay convertToRecipeUserDTO(Recipe recipe) {

        List<RecipeIngredientDTO.RecipeIngredientDisplay> ingredients = recipe.getRecipeIngredients()
                .stream().map(RecipeDTO::convertToRecipeIngredientDTO)
                .collect(Collectors.toList());

        RecipeDTO.RecipeUserDisplay recipeDTO = new RecipeDTO.RecipeUserDisplay(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getDescription(),
                recipe.getServings(),
                recipe.getTotalCost(),
                recipe.getPrepTime(),
                recipe.getCookTime(),
                ingredients,
                recipe.getImage(),
                recipe.getInstructions()
        );

        return recipeDTO;
    }

}
