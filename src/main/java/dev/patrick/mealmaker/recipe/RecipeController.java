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
    public List<Recipe> getRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping("{recipeId}")
    public Recipe getRecipe(@PathVariable Integer recipeId) {
        return recipeService.getRecipe(recipeId);
    }

    @PostMapping
    public ResponseEntity<String> addRecipe(@RequestBody RecipeRequest request) {
        return ResponseEntity.ok(recipeService.addRecipe(request).getTitle());
    }

}
