package dev.patrick.mealmaker.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import dev.patrick.mealmaker.exception.InvalidRefreshToken;
import dev.patrick.mealmaker.user.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MissingRequestCookieException;

import java.util.Arrays;
import java.util.function.Predicate;

//TODO: definitely go over this class again and try to understand the predicates, the usage of
// optional, how the stream of array works, how map function works from that stream
// and if you can do it in a way to not have to use Optional

/**
 * RefreshTokenService represents a class that handles the code for
 * generating the new access token from the request. The RefreshTokenController
 * uses an instance of this class and passes the requests from the user if
 * the request is to access a /refresh page
 */
@Service
public class RefreshTokenService {

    /**
     * UserService instance used to generate and verify JWT tokens and find users
     */
    @Autowired
    private UserService userService;

    /**
     * Generates an access token from the passed in Http request which contains
     * the refresh token as a cooke.
     * @param req The Http request from the user
     * @return The access token
     * @throws MissingRequestCookieException if the Http request doesn't contain
     *                                       a JWT cookie
     * @throws InvalidRefreshToken if the user with the given refresh token doesn't
     *                             exist or the refresh token was tampered with
     * @throws IllegalArgumentException if the cookies of the request is null
     */
    public String getAccessToken(HttpServletRequest req) throws MissingRequestCookieException {

        //The cookie objects from the request
        Cookie[] cookies = req.getCookies();
        String refreshToken = getRefreshTokenCookie(cookies);

        //Searches the database of users for the one with the given refresh token
        User foundUser = userService.findUserByRefreshToken(refreshToken);
        if (foundUser == null) {
            //If the user with the given refresh token cannot be found
            throw new InvalidRefreshToken();
        }

        //Returns the username of the person from the token and catches a JWTVerificationException
        String decodedUsername = null;
        try {
            decodedUsername = userService.verifyJWT(refreshToken);
        } catch (JWTVerificationException e) {
            throw new InvalidRefreshToken();
        }

        //If the username in the refreshToken doesn't equal the one in the
        //database then a forbidden status is given to the user
        if (!foundUser.getUsername().equals(decodedUsername)) {
            throw new InvalidRefreshToken();
        }

        //Generates a new access token using the username in the refresh token
        String accessToken = userService.getJWTToken(decodedUsername, UserService.ACCESS_TOKEN_EXPIRE);

        return accessToken;

    }

    /**
     * Gets the string of the refresh token from the cookies inside the request
     * @param cookies The cookies associated with the Http request
     * @return The refresh token as a String
     * @throws IllegalArgumentException if the cookies are null
     * @throws MissingRequestCookieException if the cookies don't contain a JWT parameter
     */
    private String getRefreshTokenCookie(Cookie[] cookies) throws MissingRequestCookieException {

        if (cookies == null) {
            throw new IllegalArgumentException("No cookies");
        }

        //I think this sets basically and if statement where it checks for
        //the input to equal "jwt"
        Predicate<Cookie> findJwtToken = t -> t.getName().equals("jwt");

        if (Arrays.stream(cookies).noneMatch(findJwtToken)) {
            throw new MissingRequestCookieException("jwt", null);
        }

        //Filters the cookies based on the predicate or "if statement" then
        //finds the first instance of that key and gets the value
        String token = Arrays.stream(cookies).
                filter(findJwtToken).
                findFirst()
                .map(c -> c.getValue())
                .get();

        return token;

    }
}
