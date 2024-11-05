package dev.patrick.mealmaker.recipe;

import jakarta.persistence.*;
import lombok.Data;

//TODO: Make the id a composite of recipe id and ingredient id
@Data
@Entity
@Table(name = "recipe_ingredient")
@IdClass(RecipeIngredientId.class)
public class RecipeIngredient {

    @Id
    @Column(name = "recipe_id")
    private Long recipeId;

    @Id
    @Column(name = "ingredient_id")
    private Long ingredientId;

    @ManyToOne
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "ingredient_id", referencedColumnName = "id")
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
