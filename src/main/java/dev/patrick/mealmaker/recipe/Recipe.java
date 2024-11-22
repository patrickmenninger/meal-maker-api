package dev.patrick.mealmaker.recipe;

import dev.patrick.mealmaker.recipe.ingredient.RecipeIngredient;
import dev.patrick.mealmaker.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String description;
    private int servings;
    private double totalCost;
    /** In minutes */
    private int prepTime;
    private int cookTime;
    private LocalDate createdDate;
    private String image;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredient> recipeIngredients;

    @OneToMany(mappedBy = "recipe")
    private List<Instruction> instructions;
//
//    public void addIngredient(Ingredient ingredient, Integer quantity, String unit) {
//        if (ingredient != null) {
//            RecipeIngredient recipeIngredient = new RecipeIngredient(this, ingredient, quantity, unit);
//            recipeIngredients.add(recipeIngredient);
//        }
//    }

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

    @Override
    public String toString() {
        return this.title;
    }

}
