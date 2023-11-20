package dev.patrick.mealmaker.repository;

import dev.patrick.mealmaker.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * UserRepository extends MongoRepository to interact with the MongoDB database
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Finds the user with the inputted username
     * @param username The username to search for
     * @return The User object
     */
    @Query("{'username' : ?0}")
    User findByUsername(String username);

    /**
     * Finds the user with the inputted refreshToken
     * @param refreshToken The refreshToken to search for
     * @return The User object
     */
    @Query("{'refreshToken' : ?0}")
    User findByRefreshToken(String refreshToken);

}
