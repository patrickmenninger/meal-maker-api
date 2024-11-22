package dev.patrick.mealmaker.recipe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipeRequest {

    private Integer userId;
    private String title;
    private String description;
    private Integer servings;
    private Double totalCost;
    private Integer prepTime;
    private Integer cookTime;
    private String image;
    private List<String> ingredientNames;
    private List<Integer> quantities;
    private List<String> units;
    private List<String> instructionTexts;

}
