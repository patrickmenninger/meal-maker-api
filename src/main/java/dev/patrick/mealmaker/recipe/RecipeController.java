package dev.patrick.mealmaker.recipe;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping
    public List<RecipeDTO.RecipeDisplay> getRecipes(RecipeRequest recipeRequest) {

        Sort sortWay;
        if (recipeRequest.getSort() == null) {
            sortWay = Sort.unsorted();
        } else {
            sortWay = Sort.by(Sort.Order.by(recipeRequest.getSort().toString())
                    .with(Sort.Direction.fromString(recipeRequest.getDirec())));
        }

        Pageable pageRequest = PageRequest.of(recipeRequest.getPage(), 2, sortWay);

        return recipeService.getAllRecipes(pageRequest, recipeRequest);
    }

    @GetMapping("{recipeId}")
    public RecipeDTO.RecipeDisplay getRecipe(@PathVariable Integer recipeId) {
        return recipeService.getRecipe(recipeId);
    }

    @PostMapping
    public /*RecipeDTO.RecipeDisplay*/ ResponseEntity addRecipe(@RequestBody RecipeAddRequest request) {
        recipeService.addRecipe(request);
        return ResponseEntity.ok().build();
    }

}
