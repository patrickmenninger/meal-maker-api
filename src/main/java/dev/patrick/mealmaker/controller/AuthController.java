package dev.patrick.mealmaker.controller;

import dev.patrick.mealmaker.exception.InvalidPasswordException;
import dev.patrick.mealmaker.exception.UsernameNotFoundException;
import dev.patrick.mealmaker.service.AuthService;
import dev.patrick.mealmaker.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * TODO: Javadoc
 */
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/")
public class AuthController {

    @Autowired
    AuthService authService;

    /**
     * Logins a user when they make a request to "/login" path. The user is
     * logged in through the logic in UserService class.
     * @param payload The information passed in from the form on the website containing
     *                the username and password of the user.
     * @param response The Http response that is sent back to the user when the code is executed
     * @return The response sent back to the user
     */
    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> payload, HttpServletResponse response) {

        ResponseEntity<String> responseEntity;
        User foundUser;
        String resBody;

        try {

            //Searches for the user in the database if they exist and the password is correct
            foundUser = authService.login(payload.get("username"), payload.get("password"), response);
            resBody = foundUser.getAccessToken();
            responseEntity = new ResponseEntity<>(resBody, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
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

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {

        String resBody = null;

        try {

            resBody = authService.logout(request, response).getUsername();

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(resBody, HttpStatus.NO_CONTENT);

    }

}
