package dev.patrick.mealmaker.recipe;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private int servings;
    private int cookTime;
    private double totalCost;
    private double servingCost;
    private String image;

}
