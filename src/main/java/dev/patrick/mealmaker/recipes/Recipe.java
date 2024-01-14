package dev.patrick.mealmaker.recipes;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Recipe class holds information about the title of the recipe,
 * the amount of servings, the cook time, the total cost, the
 * cost per serving, the nutrition facts, and the ingredients
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Document(collection = "recipes")
public class Recipe {

    /** The specific id of the recipe in the db */
    @Id
    private ObjectId id;
    /** The title of the recipe */
    private String title;
    /** The amount of servings of the recipe */
    private int servings;
    /** The cook time of the recipe */
    private int cookTime;
    /** The total cost of the recipe */
    private double totalCost;
    /** The cost per serving of the recipe */
    private double servingCost;
    /** The nutrition facts of the recipe */
    private Nutrition nutrition;
    /** The list of ingrediants of the recipe */
    private List<Ingredient> ingredients;
    /** The link to the image of the meal */
    private String image;



}
