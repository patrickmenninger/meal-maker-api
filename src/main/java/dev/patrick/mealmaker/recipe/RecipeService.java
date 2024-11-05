package dev.patrick.mealmaker.recipe;

import dev.patrick.mealmaker.exception.ResourceNotFoundException;
import dev.patrick.mealmaker.user.User;
import dev.patrick.mealmaker.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final UserRepository userRepository;

    private final RecipeRepository recipeRepository;

    private final IngredientRepository ingredientRepository;

    private final InstructionRepository instructionRepository;

    private final RecipeIngredientRepository recipeIngredientRepository;


    public List<Recipe> getAllRecipes() {

        return recipeRepository.findAll();

    }

    public Recipe getRecipe(Integer recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Recipe not found"
                ));
    }

//    @Transactional
    public Recipe addRecipe(RecipeRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found"
                ));

        Recipe recipe = Recipe.builder()
                .user(user)
                .title(request.getTitle())
                .description(request.getDescription())
                .servings(request.getServings())
                .totalCost(request.getTotalCost())
                .prepTime(request.getPrepTime())
                .cookTime(request.getCookTime())
                .image(request.getImage())
                .build();

        /** Adds all ingredients and updates the relationship table */
        Ingredient ingredient = ingredientRepository.findByName(request.getIngredientNames().get(0))
                .orElseThrow();

        RecipeIngredient recipeIngredient = new RecipeIngredient();
        RecipeIngredientId id = new RecipeIngredientId();
        id.setRecipeId(recipe.getId());
        id.setIngredientId(ingredient.getId());

        recipeIngredient.setId(id);
        recipeIngredient.setRecipe(recipe);
        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setQuantity(request.getQuantities().get(0));
        recipeIngredient.setAmount(request.getUnits().get(0));

        /** Saves the recipe so the id can be referenced in the join table */
        recipeRepository.save(recipe);

        /**
         * Saves the entry in the join table with a reference to recipe and the ingredient
         * You don't need to save the ingredient because it is already in the db and
         * you're not changing it
         */
        recipeIngredientRepository.save(recipeIngredient);

        for (int i = 0; i < request.getInstructionTexts().size(); i++) {

            Instruction instruction = Instruction.builder()
                    .stepNumber(i + 1)
                    .instructionText(request.getInstructionTexts().get(i))
                    .recipe(recipe)
                    .build();

            /** Save the new instruction with a reference to the recipe */
            instructionRepository.save(instruction);
        }

        return recipe;

    }
}
