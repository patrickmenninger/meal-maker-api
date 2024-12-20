package dev.patrick.mealmaker.recipe;

import dev.patrick.mealmaker.exception.ResourceNotFoundException;
import dev.patrick.mealmaker.recipe.ingredient.*;
import dev.patrick.mealmaker.user.User;
import dev.patrick.mealmaker.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final UserRepository userRepository;

    private final RecipeRepository recipeRepository;

    private final InstructionRepository instructionRepository;

    private final RecipeIngredientRepository recipeIngredientRepository;


    public List<RecipeDTO.RecipeDisplay> getAllRecipes(Pageable pageRequest, RecipeRequest recipeRequest) {

            if (recipeRequest.getTitle() != null ||
                    recipeRequest.getMinCookTime() != null || recipeRequest.getMaxCookTime() != null ||
                    recipeRequest.getMinTotalCost() != null || recipeRequest.getMaxTotalCost() != null ||
                    recipeRequest.getStartDate() != null || recipeRequest.getEndDate() != null
            ) {

                return recipeRepository.findByFilters(recipeRequest.getTitle(),
                        recipeRequest.getMinTotalCost(),
                        recipeRequest.getMaxTotalCost(),
                        recipeRequest.getMinCookTime(),
                        recipeRequest.getMaxCookTime(),
                        recipeRequest.getStartDate(),
                        recipeRequest.getEndDate(),
                        pageRequest)
                        .stream().map(RecipeDTO::convertToRecipeDTO)
                        .collect(Collectors.toList());

            } else {

                return recipeRepository.findAll(pageRequest)
                        .stream().map(RecipeDTO::convertToRecipeDTO)
                        .collect(Collectors.toList());
            }

    }

    public RecipeDTO.RecipeDisplay getRecipe(Integer recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Recipe not found"
                ));

        return RecipeDTO.convertToRecipeDTO(recipe);
    }

    @Transactional
    public /*RecipeDTO.RecipeDisplay*/ void addRecipe(RecipeAddRequest request) {
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
                .createdDate(LocalDate.now())
                .build();

        /** Saves the recipe so the id can be referenced in the join table */
        recipeRepository.save(recipe);

        /** Adds all ingredients and updates the relationship table */
        for (int i = 0; i < request.getIngredientNames().size(); i++) {

            RecipeIngredient recipeIngredient = new RecipeIngredient();

            recipeIngredient.setRecipe(recipe);
            recipeIngredient.setName(request.getIngredientNames().get(i));
            recipeIngredient.setAmount(request.getAmount().get(i));

            /**
             * Saves the entry in the join table with a reference to recipe and the ingredient
             * You don't need to save the ingredient because it is already in the db and
             * you're not changing it
             */
            recipeIngredientRepository.save(recipeIngredient);
        }

        for (int i = 0; i < request.getInstructionTexts().size(); i++) {

            Instruction instruction = Instruction.builder()
                    .stepNumber(i + 1)
                    .instructionText(request.getInstructionTexts().get(i))
                    .recipe(recipe)
                    .build();

            /** Save the new instruction with a reference to the recipe */
            instructionRepository.save(instruction);
        }

//        return RecipeDTO.convertToRecipeDTO(recipe);
    }

}
