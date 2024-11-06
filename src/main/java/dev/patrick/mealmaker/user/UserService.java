package dev.patrick.mealmaker.user;

import dev.patrick.mealmaker.exception.ResourceNotFoundException;
import dev.patrick.mealmaker.recipe.Recipe;
import dev.patrick.mealmaker.recipe.RecipeDTO;
import dev.patrick.mealmaker.recipe.ingredient.RecipeIngredient;
import dev.patrick.mealmaker.recipe.ingredient.RecipeIngredientDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserDTO.UserDisplay> getAllUsers() {
        return userRepository.findAll()
                .stream().map(UserDTO::convertToUserDTO)
                .collect(Collectors.toList());

    }

    public UserDTO.UserDisplay getUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found"
                ));

        return UserDTO.convertToUserDTO(user);
    }

}
