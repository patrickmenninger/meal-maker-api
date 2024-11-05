package dev.patrick.mealmaker.recipe;

import dev.patrick.mealmaker.exception.ResourceNotFoundException;
import dev.patrick.mealmaker.user.User;
import dev.patrick.mealmaker.user.UserDTO;
import dev.patrick.mealmaker.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    private final RecipeIngredientRepository recipeIngredientRepository;


    public List<RecipeDTO.RecipeDisplay> getAllRecipes() {

        return recipeRepository.findAll()
                .stream().map(this::convertToRecipeDTO)
                .collect(Collectors.toList());

    }

    public RecipeDTO.RecipeDisplay getRecipe(Integer recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Recipe not found"
                ));

        return convertToRecipeDTO(recipe);
    }

//    @Transactional
    public RecipeDTO.RecipeDisplay addRecipe(RecipeRequest request) {
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

        return convertToRecipeDTO(recipe);

    }

    private RecipeDTO.RecipeDisplay convertToRecipeDTO(Recipe recipe) {

        UserDTO.UserRecipeDisplay userDTO = convertToUserRecipeDTO(recipe.getUser());

        return new RecipeDTO.RecipeDisplay(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getDescription(),
                recipe.getServings(),
                recipe.getTotalCost(),
                recipe.getPrepTime(),
                recipe.getCookTime(),
                recipe.getImage(),
                userDTO
        );
    }

    private UserDTO.UserRecipeDisplay convertToUserRecipeDTO(User user) {

        List<Integer> recipes = user.getRecipes().stream()
                .map(recipe -> recipe.getId())
                .collect(Collectors.toList());

        return new UserDTO.UserRecipeDisplay(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                recipes
        );
    }
}
