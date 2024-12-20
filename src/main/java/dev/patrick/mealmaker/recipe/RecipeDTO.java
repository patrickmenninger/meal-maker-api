package dev.patrick.mealmaker.recipe;

import dev.patrick.mealmaker.recipe.ingredient.RecipeIngredient;
import dev.patrick.mealmaker.recipe.ingredient.RecipeIngredientDTO;
import dev.patrick.mealmaker.user.User;
import dev.patrick.mealmaker.user.UserDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeDTO {

    public record RecipeDisplay(
            Integer id,
            String title,
            String description,
            Integer servings,
            Double totalCost,
            Integer prepTime,
            Integer cookTime,
            LocalDate createdDate,
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
            LocalDate createdDate,
            List<RecipeIngredientDTO.RecipeIngredientDisplay> ingredients,
            String image,
            List<Instruction> instructions
    ) {}

    public static RecipeDTO.RecipeDisplay convertToRecipeDTO(Recipe recipe) {

        UserDTO.UserRecipeDisplay userDTO = convertToUserRecipeDTO(recipe.getUser());

        List<RecipeIngredientDTO.RecipeIngredientDisplay> ingredientsDTO = recipe.getRecipeIngredients()
                .stream().map(RecipeDTO::convertToRecipeIngredientDTO)
                .collect(Collectors.toList());

        return new RecipeDTO.RecipeDisplay(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getDescription(),
                recipe.getServings(),
                recipe.getTotalCost(),
                recipe.getPrepTime(),
                recipe.getCookTime(),
                recipe.getCreatedDate(),
                ingredientsDTO,
                recipe.getImage(),
                userDTO,
                recipe.getInstructions()
        );
    }

    public static UserDTO.UserRecipeDisplay convertToUserRecipeDTO(User user) {

        List<Integer> recipes = user.getRecipes().stream()
                .map(recipe -> recipe.getId())
                .collect(Collectors.toList());

        return new UserDTO.UserRecipeDisplay(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                recipes
        );
    }

    public static RecipeIngredientDTO.RecipeIngredientDisplay convertToRecipeIngredientDTO(
            RecipeIngredient recipeIngredient) {

        return new RecipeIngredientDTO.RecipeIngredientDisplay(
                recipeIngredient.getName(),
                recipeIngredient.getAmount()
        );

    }

}
