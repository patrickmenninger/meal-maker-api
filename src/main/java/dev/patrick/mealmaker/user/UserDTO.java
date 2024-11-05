package dev.patrick.mealmaker.user;

import dev.patrick.mealmaker.recipe.Recipe;

import java.util.List;

public record UserDTO(
        Integer id,
        String firstname,
        String lastname,
        String email,
        Role role
) {

}
