package dev.patrick.mealmaker.recipes;

import lombok.*;

/**
 * Ingredient class represents an ingredient used to make food.
 * Each ingredient contains information about the type of ingredient,
 * the name of the ingredient, and the amount.
 */
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Ingredient {

    /** The type of the ingredient */
    //TODO: make this an enum
    private String type;
    /** The name of the ingredient */
    private String name;
    /** The amount of the ingredient */
    private String amount;
    
}
