package dev.patrick.mealmaker.repository;

import dev.patrick.mealmaker.recipes.Recipe;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * RecipeRepository extends MongoRepository and it used to interact with MongoDB
 */
@Repository
public interface RecipeRepository extends MongoRepository<Recipe, ObjectId> {

}
