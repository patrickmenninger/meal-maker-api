package dev.patrick.mealmaker.recipe;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "nutrition")
public class Nutrition {

    @Id
    private Integer recipeId;
    private Integer calories;
    private Integer carbs;
    private Integer protein;
    private Integer fat;
    private Double fiber;
    private Integer sodium;

}
