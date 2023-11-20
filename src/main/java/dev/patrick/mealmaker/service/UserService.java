package dev.patrick.mealmaker.service;

import dev.patrick.mealmaker.repository.UserRepository;
import dev.patrick.mealmaker.user.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for the User objects. It deals with all the business logic
 * associated with the user's requests from the website.
 */
@Service
public class UserService {

    /** The instance of the repository to look at the databse */
    @Autowired
    private UserRepository userRepository;

    /** The instance of the auth service to hash the password */
    @Autowired
    private AuthService authService;

    /**
     * Registers a new user to the database
     * @param username The username of the new user
     * @param password The password of the new user
     * @return The User that was created and added
     */
    public User addUser(String username, String password) {

        //Searches to see if the username already exists
        if (userRepository.findByUsername(username) != null) {
            throw new IllegalArgumentException();
        }

        //Hashes the password
        String pw_hash = authService.hashPw(password);

        //Adds the user to the repository or database
        User user = userRepository.insert(new User(username, pw_hash));

        return user;

    }

    /**
     * Finds the user in the database by using the repository and the refresh token
     * @param refreshToken The refresh token to look for
     * @return The User associated with the refresh token
     */
    public User findUserByRefreshToken(String refreshToken) {
        return userRepository.findByRefreshToken(refreshToken);
    }

    /**
     * Gets all the users from the database and checks to ensure
     * the user trying to access the information is allowed to base
     * on their JWT token.
     * @param request The Http request that is sent by the user
     * @return The list of users
     * @throws IllegalArgumentException if the authorization header of the request is null
     */
    public List<User> getAllUsers(HttpServletRequest request) {

        if (request.getHeader("authorization") == null) {
            throw new IllegalArgumentException();
        }

        String authHeader = request.getHeader("authorization").split(" ")[1];

        //TODO: Change this so it checks to make sure the user's role allows them to see this info
        authService.verifyJWT(authHeader);

        return userRepository.findAll();
    }

    //TODO: get rid of because it is for testing purposes
    public void deleteUsers() {
        userRepository.deleteAll();
    }

}
