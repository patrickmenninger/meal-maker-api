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
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * TODO: Javadoc
 */
@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/")
public class AuthController {

    @Autowired
    AuthService authService;

    /**
     * Log ins a user when they make a request to "/login" path. The user is
     * logged in through the logic in UserService class.
     * @param payload The information passed in from the form on the website containing
     *                the username and password of the user.
     * @param response The Http response that is sent back to the user when the code is executed
     * @return The response sent back to the user
     */
    @PostMapping("login")
    public ResponseEntity<User> login(@RequestBody Map<String, String> payload, HttpServletResponse response) {

        ResponseEntity<User> responseEntity;
        User foundUser;

        try {

            //Searches for the user in the database if they exist and the password is correct
            foundUser = authService.login(payload.get("username"), payload.get("password"), response);
            responseEntity = new ResponseEntity<>(foundUser, HttpStatus.OK);

        } catch (AuthenticationCredentialsNotFoundException e) {
            //If one of the inputs or both are empty
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (UsernameNotFoundException e) {
            //If the username cannot be found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (InvalidPasswordException e) {
            //If the password doesn't match the one for the given user
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return responseEntity;

    }

    /**
     * Logs out a user when they make a request to "/logout". The
     * business logic is handeled in the service class which deletes the
     * refresh token and acess token
     * @param request The Http request with the cookies
     * @param response The Http request without the jwt cookie
     * @return The response with status 204 and the username of the logged-out user
     */
    @GetMapping("logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {

        try {

            authService.logout(request, response);

        } catch (IllegalArgumentException e) {
            //Do nothing
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
