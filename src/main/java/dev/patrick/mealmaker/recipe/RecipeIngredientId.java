package dev.patrick.mealmaker.recipe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeIngredientId implements Serializable {

    private Integer recipeId;
    private Integer ingredientId;

}
