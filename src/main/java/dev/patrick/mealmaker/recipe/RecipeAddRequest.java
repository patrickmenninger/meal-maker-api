package dev.patrick.mealmaker.recipe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipeAddRequest {

    private Integer userId;
    private String title;
    private String description;
    private Integer servings;
    private Double totalCost;
    private Integer prepTime;
    private Integer cookTime;
    private String image;
    private List<String> ingredientNames;
    private List<String> amount;
    private List<String> instructionTexts;

}
