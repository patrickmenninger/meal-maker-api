package dev.patrick.mealmaker.recipe.ingredient;

public class RecipeIngredientDTO {

    public record RecipeIngredientDisplay(
       String name,
       String amount
    ) {}

}
