package dev.patrick.mealmaker.recipe;

import dev.patrick.mealmaker.exception.ResourceNotFoundException;
import dev.patrick.mealmaker.user.User;
import dev.patrick.mealmaker.user.UserDTO;
import dev.patrick.mealmaker.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final UserRepository userRepository;

    private final RecipeRepository recipeRepository;

    private final IngredientRepository ingredientRepository;

    private final InstructionRepository instructionRepository;


    public List<Recipe> getAllRecipes() {

        return recipeRepository.findAll();

    }

    public Recipe getRecipe(Integer recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Recipe not found"
                ));
    }

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
                .recipeIngredients(new HashSet<>())
                .build();

        /** Adds all ingredients and updates the relationship table */
        for (int i = 0; i < request.getIngredientNames().size(); i++) {

            //TODO: Could change this logic to create new ingredient
            Ingredient ingredient = ingredientRepository.findByName(request.getIngredientNames().get(i))
                    .orElseThrow(() -> new RuntimeException("Ingredient not found"));

            recipe.addIngredient(ingredient, request.getQuantities().get(i), request.getUnits().get(i));
        }

        recipeRepository.save(recipe);

        for (int i = 0; i < request.getInstructionTexts().size(); i++) {

            Instruction instruction = Instruction.builder()
                    .stepNumber(i + 1)
                    .instructionText(request.getInstructionTexts().get(i))
                    .recipe(recipe)
                    .build();

            instructionRepository.save(instruction);
        }

        return recipe;

    }
}
