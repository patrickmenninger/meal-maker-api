package dev.patrick.mealmaker.recipe;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RecipeIngredient {

    @EmbeddedId
    private RecipeIngredientId id;

    @ManyToOne
    @MapsId("recipeId")
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    private Integer quantity;
    private String amount;

    public RecipeIngredient(Recipe recipe, Ingredient ingredient, Integer quantity, String amount) {
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.amount = amount;
    }

}
