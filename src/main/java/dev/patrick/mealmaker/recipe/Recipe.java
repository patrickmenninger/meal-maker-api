package dev.patrick.mealmaker.recipe;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dev.patrick.mealmaker.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * Recipe class holds information about the title of the recipe,
 * the amount of servings, the cook time, the total cost, the
 * cost per serving, the nutrition facts, and the ingredients
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "recipes")

public class Recipe {

    /** The specific id of the recipe in the db */
    @Id
    @GeneratedValue
    private Integer id;
    private String title;
    private String description;
    private int servings;
    private double totalCost;
    /** In minutes */
    private int prepTime;
    private int cookTime;
    private String image;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "recipe")
    private Set<RecipeIngredient> recipeIngredients = new HashSet<>();

    @OneToMany(mappedBy = "recipe")
    private List<Instruction> instructions;

    public void addIngredient(Ingredient ingredient, Integer quantity, String unit) {
        RecipeIngredient recipeIngredient = new RecipeIngredient(this, ingredient, quantity, unit);
        recipeIngredients.add(recipeIngredient);
    }

    public void removeIngredient(Ingredient ingredient) {
        recipeIngredients.removeIf(ri -> ri.getIngredient().equals(ingredient));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return this.id.equals(recipe.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
