package dev.patrick.mealmaker.user;

import dev.patrick.mealmaker.exception.ResourceNotFoundException;
import dev.patrick.mealmaker.recipe.Recipe;
import dev.patrick.mealmaker.recipe.RecipeDTO;
import dev.patrick.mealmaker.recipe.RecipeIngredient;
import dev.patrick.mealmaker.recipe.RecipeIngredientDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserDTO.UserDisplay> getAllUsers() {
        return userRepository.findAll()
                .stream().map(this::convertToUserDTO)
                .collect(Collectors.toList());

    }

    public UserDTO.UserDisplay getUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found"
                ));

        return convertToUserDTO(user);
    }

    private UserDTO.UserDisplay convertToUserDTO(User user) {

        List<RecipeDTO.RecipeUserDisplay> recipes = user.getRecipes().stream()
                .map(this::convertToRecipeUserDTO)
                .collect(Collectors.toList());

        UserDTO.UserDisplay userDTO = new UserDTO.UserDisplay(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getRole(),
                recipes
        );

        return userDTO;

    }

    private RecipeDTO.RecipeUserDisplay convertToRecipeUserDTO(Recipe recipe) {

        List<RecipeIngredientDTO.RecipeIngredientDisplay> ingredients = recipe.getRecipeIngredients()
                .stream().map(this::convertToRecipeIngredientDTO)
                .collect(Collectors.toList());

        RecipeDTO.RecipeUserDisplay recipeDTO = new RecipeDTO.RecipeUserDisplay(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getDescription(),
                recipe.getServings(),
                recipe.getTotalCost(),
                recipe.getPrepTime(),
                recipe.getCookTime(),
                ingredients,
                recipe.getImage(),
                recipe.getInstructions()
        );

        return recipeDTO;
    }

    private RecipeIngredientDTO.RecipeIngredientDisplay convertToRecipeIngredientDTO(
            RecipeIngredient recipeIngredient) {

        return new RecipeIngredientDTO.RecipeIngredientDisplay(
                recipeIngredient.getIngredient().getName(),
                recipeIngredient.getQuantity(),
                recipeIngredient.getAmount()
        );

    }

}
