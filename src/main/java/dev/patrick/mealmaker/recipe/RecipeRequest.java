package dev.patrick.mealmaker.recipe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipeRequest {

    private SortType sort;
    private String direc;
    private Integer page = 0;

    private String title;
    private Double minTotalCost;
    private Double maxTotalCost;
    private Integer minCookTime;
    private Integer maxCookTime;
    private LocalDate startDate;
    private LocalDate endDate;

}
