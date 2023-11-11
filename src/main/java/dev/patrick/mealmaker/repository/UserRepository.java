package dev.patrick.mealmaker.repository;

import dev.patrick.mealmaker.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Looks for a user based on the inputted username
     * @param username The username to search for
     * @return The User object
     */
    @Query("{'username' : ?0}")
    User findByUsername(String username);

    //TODO: find what this "?0" is
    @Query("{'refreshToken' : ?0}")
    User findByRefreshToken(String refreshToken);

}
