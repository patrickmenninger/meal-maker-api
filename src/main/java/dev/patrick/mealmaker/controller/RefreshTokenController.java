package dev.patrick.mealmaker.controller;

import dev.patrick.mealmaker.exception.InvalidRefreshToken;
import dev.patrick.mealmaker.service.RefreshTokenService;
import dev.patrick.mealmaker.user.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RefreshTokenController represents the controller class that handles
 * the requests from the user and then the code related to validating refresh tokens
 * and creating new access tokens from said refresh tokens. It has an instance of
 * RefreshTokenService in order to actually execute the code.
 */
@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/refresh")
public class RefreshTokenController {

    /** The instance of RefreshTokenService so requests can be handled */
    @Autowired
    private RefreshTokenService refreshTokenService;

    /**
     * Generates an access token from using the Http request which contains the
     * refresh tokens in its cookies.
     * @param req The Http request from the user
     * @return The response with the access token in the body
     *          and the status depending on whether an exception was thrown
     */
    @GetMapping
    public ResponseEntity<User> getAccessToken(HttpServletRequest req) {


        try {
            //Sets the response of the http request to be the access token
            User user = refreshTokenService.getAccessToken(req);

            /*
             * If there are no problems validating the refresh token and
             * generating the new access token
             */
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            //If the request doesn't contain a refresh token
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (InvalidRefreshToken e) {
            /*
             * If the refresh token was tampered with or the user with the given
             * refresh token doesn't exist
             */
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

}
