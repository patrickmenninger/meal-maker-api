package dev.patrick.mealmaker.user;

public record UserDTO(
        Integer id,
        String firstname,
        String lastname,
        String email,
        Role role
) {

}
