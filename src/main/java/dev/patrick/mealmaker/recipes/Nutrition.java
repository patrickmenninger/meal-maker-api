package dev.patrick.mealmaker.recipes;

import lombok.*;

/**
 * Nutrition represents the nutrition facts associated with a food item.
 * Each food item contains information about the amount of servings, the
 * calories per serving, the carbs per serving, the protein per serving,
 * the fat per serving, the sodium per serving, and the fiber per serving.
 */
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Nutrition {

    /** The number of servings */
    private int servings;
    /** The amount of calories per serving */
    private int calories;
    /** The amount of carbs per serving */
    private int carbs;
    /** The amount of protein per serving */
    private int protein;
    /** The amount of fat per serving */
    private int fat;
    /** The amount of sodium per serving */
    private int sodium;
    /** The amount of fiber per serving */
    private int fiber;

}
