package dev.patrick.mealmaker.recipe;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping
    public List<RecipeDTO.RecipeDisplay> getRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping("{recipeId}")
    public RecipeDTO.RecipeDisplay getRecipe(@PathVariable Integer recipeId) {
        return recipeService.getRecipe(recipeId);
    }

    @PostMapping
    public /*RecipeDTO.RecipeDisplay*/ ResponseEntity addRecipe(@RequestBody RecipeRequest request) {
        recipeService.addRecipe(request);
        return ResponseEntity.ok().build();
    }

}
