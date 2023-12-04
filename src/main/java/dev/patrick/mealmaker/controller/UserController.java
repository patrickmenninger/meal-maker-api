package dev.patrick.mealmaker.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import dev.patrick.mealmaker.exception.InvalidPasswordException;
import dev.patrick.mealmaker.exception.UsernameNotFoundException;
import dev.patrick.mealmaker.service.UserService;
import dev.patrick.mealmaker.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * UserController represents the controller class that handles the user's
 * requests related to a user. These include registering and logging in. It
 * has an instance of UserService in order to perform the business logic
 * associated with each request.
 */
@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/")
public class UserController {

    /** Instance of UserService used to perform business logic */
    @Autowired
    private UserService userService;

    /**
     * Registers a new user when they make a request to the "/register" path.
     * @param payload The information passed in from the form on the website
     *                containing the username and password.
     * @return The Html response with the username returned in the body and HttpStatus 201
     */
    @PostMapping("/register")
    public ResponseEntity<String> addUser(@RequestBody Map<String, String> payload) {

        ResponseEntity<String> response;
        String resBody;

        try {

            //Adds the user to the database through the UserService class. The information is got
            //using key-value pairs where the key username related to the new username and so on
            User currentUser = userService.addUser(payload.get("username"), payload.get("password"));
            resBody = "New user " + currentUser.getUsername() + " created.";
            response = new ResponseEntity<>(resBody, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            //If the username is already taken then a conflict status is sent back
            resBody = "Username is taken.";
            return new ResponseEntity<>(resBody, HttpStatus.CONFLICT);
        } catch (AuthenticationCredentialsNotFoundException e) {
            //If the username or password are empty
            resBody = "Username and password required.";
            return new ResponseEntity<>(resBody, HttpStatus.BAD_REQUEST);
        }


        return response;
    }

    /**
     * Gets the list of all users. The person accessing needs to
     * have a role of editor in order to see the users.
     * @return The response with the list of users
     */
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(HttpServletRequest request) {
        List<User> userList = null;

        try {
            userList = userService.getAllUsers(request);
        } catch (JWTVerificationException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        //TODO: Change how the users are returned because right now the access token and
        //TODO: refresh token are being returned with the user information when they shouldn't be
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    //TODO: get rid of this, just for testing purposes
    @PostMapping("/deleteAll")
    public void deleteUsers() {

            userService.deleteUsers();

    }



}
