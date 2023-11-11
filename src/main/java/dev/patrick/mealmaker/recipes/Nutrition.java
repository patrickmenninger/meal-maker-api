package dev.patrick.mealmaker.recipes;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Nutrition {

    private int servings;
    private int calories;
    private int carbs;
    private int protein;
    private int fat;
    private int sodium;
    private int fiber;

}
