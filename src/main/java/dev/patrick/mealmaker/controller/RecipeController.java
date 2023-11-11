package dev.patrick.mealmaker.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import dev.patrick.mealmaker.recipes.Recipe;
import dev.patrick.mealmaker.service.RecipeService;
import dev.patrick.mealmaker.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The RecipeController class is the one that the user "interacts" with
 * by accessing the webpage and requesting the data
 */
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/recipes")
public class RecipeController {

    /**
     * The instance of RecipeService is what interacts with the repository to
     * access the database
     */
    @Autowired
    private RecipeService recipeService;

    /**
     * The instance of UserService to validate the user's access to the database
     */
    @Autowired
    private UserService userService;

    /**
     * Gets all the recipes from the database from the service which uses the repository
     * @return The list of all movies
     */
    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes(HttpServletRequest req) {

        List<Recipe> recipeList;

        try {

            recipeList = recipeService.allRecipes(userService, req);

        } catch (JWTVerificationException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(recipeList, HttpStatus.OK);

    }

}
