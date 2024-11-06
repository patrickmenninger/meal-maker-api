package dev.patrick.mealmaker.recipe;

public class RecipeIngredientDTO {

    public record RecipeIngredientDisplay(
       String name,
       Integer quantity,
       String amount
    ) {}

}
