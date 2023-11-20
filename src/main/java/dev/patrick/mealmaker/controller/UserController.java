package dev.patrick.mealmaker.controller;

import dev.patrick.mealmaker.exception.InvalidPasswordException;
import dev.patrick.mealmaker.exception.InvalidRefreshToken;
import dev.patrick.mealmaker.exception.UsernameNotFoundException;
import dev.patrick.mealmaker.service.UserService;
import dev.patrick.mealmaker.user.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * UserController represents the controller class that handles the user's
 * requests related to a user. These include registering and logging in. It
 * has an instance of UserService in order to perform the business logic
 * associated with each request.
 */
@RestController
@CrossOrigin(origins = "http://localhost:3000")
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
        }


        return response;
    }

    /**
     * Logins a user when they make a request to "/login" path. The user is
     * logged in through the logic in UserService class.
     * @param payload The information passed in from the form on the website containing
     *                the username and password of the user.
     * @param response The Http response that is sent back to the user when the code is executed
     * @return The response sent back to the user
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> payload, HttpServletResponse response) {

        ResponseEntity<String> responseEntity;
        User foundUser;
        String resBody;

        try {

            //Searches for the user in the database if they exist and the password is correct
            foundUser = userService.login(payload.get("username"), payload.get("password"), response);
            resBody = foundUser.getAccessToken();
            responseEntity = new ResponseEntity<>(resBody, HttpStatus.OK);

        } catch (IllegalArgumentException e){
            //If one of the inputs or both are empty
            resBody = "Username and password required.";
            return new ResponseEntity<>(resBody, HttpStatus.BAD_REQUEST);
        } catch (UsernameNotFoundException e) {
            //If the username cannot be found
            resBody = "Username not found.";
            return new ResponseEntity<>(resBody, HttpStatus.NOT_FOUND);
        } catch (InvalidPasswordException e) {
            //If the password doesn't match the one for the given user
            resBody = "Invalid password.";
            return new ResponseEntity<>(resBody, HttpStatus.UNAUTHORIZED);
        }

        return responseEntity;

    }

    //TODO: get rid of this, just for testing purposes
    @PostMapping("/deleteAll")
    public void deleteUsers() {
        userService.deleteUsers();
    }



}
