//package dev.patrick.mealmaker.service;
//
//import dev.patrick.mealmaker.recipe.Recipe;
//import dev.patrick.mealmaker.repository.CustomRecipeRepository;
//import dev.patrick.mealmaker.repository.RecipeRepository;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
///**
// * Service class for recipes that deals with the business behind
// * retrieving recipes
// */
//@Service
//public class RecipeService {
//
//    /**
//     * Recipe repository
//     */
//    @Autowired
//    private RecipeRepository recipeRepository;
//
//    @Autowired
//    private CustomRecipeRepository customRecipeRepository;
//
//    /**
//     * Finds all the recipe objects
//     * @return The recipe objects as a list
//     */
//    public List<Recipe> allRecipes(AuthController authService, HttpServletRequest req) {
//
//        if (req.getHeader("authorization") == null) {
//            throw new IllegalArgumentException();
//        }
//
//        String authHeader = req.getHeader("authorization").split(" ")[1];
//        //TODO: Figure out what to do with the username here or switch the return type
//        //TODO: Could maybe get rid of this check and allow the recipes to be open
//        authService.verifyJWT(authHeader);
//
//        return customRecipeRepository.findRecipes(req);
//    }
//
//}
