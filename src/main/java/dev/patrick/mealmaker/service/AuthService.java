package dev.patrick.mealmaker.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import dev.patrick.mealmaker.exception.InvalidPasswordException;
import dev.patrick.mealmaker.exception.UsernameNotFoundException;
import dev.patrick.mealmaker.repository.UserRepository;
import dev.patrick.mealmaker.user.User;
import dev.patrick.mealmaker.util.ArrayList;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * TODO: Javadoc
 */
@Service
public class AuthService {

    /** Date used to describe when the access token expires which is 10 seconds */
    public static final int ACCESS_TOKEN_EXPIRE = 10000;
    /** Date used to describe when the refresh token expires which is 1 day */
    public static final int REFRESH_TOKEN_EXPIRE = 86400000;

    /** The number of seconds in a day */
    private static final int SECONDS_IN_DAY = 86400;

    //TODO: figure out what key to use and where to put it
    /** The algorithm used to encode the JWT token */
    private Algorithm algorithm = Algorithm.HMAC256("secret");
    /** The encoder used to encode the JWT token */
    private static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);

    /** The instance of UserRepository to find the users */
    @Autowired
    private UserRepository userRepository;

    /**
     * Logs in the user based on the inputted username and password
     * @param username The username to log in
     * @param password The password to log in with
     * @param response The Http response used to add cookies to it
     * @return The User that was logged in
     */
    public User login(String username, String password, HttpServletResponse response) {

        //Makes sure the username and password aren't null
        if (username == null || username.equals("") || password == null || password.equals("")) {
            throw new AuthenticationCredentialsNotFoundException("Username and password are required.");
        }

        //Finds the user in the database
        User foundUser = userRepository.findByUsername(username);

        //If they don't exist then exception is thrown
        if (foundUser == null) {
            throw new UsernameNotFoundException(username + " does not exist.");
        }

        //If the inputted password matches the password associated with the user
        if (bCryptPasswordEncoder.matches(password, foundUser.getPassword())) {

            //Expires in 10 seconds
            String accessToken = getJWTToken(foundUser, new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE));
            //Expires after 1 day
            String refreshToken = getJWTToken(foundUser, new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE));

            //Updates the User's fields and then the new information is saved to the database
            foundUser.setAccessToken(accessToken);
            foundUser.setRefreshToken(refreshToken);
            userRepository.save(foundUser);

            //Creates the cookie and then adds it to the response
            Cookie cookie = new Cookie("jwt", refreshToken);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(SECONDS_IN_DAY);
            cookie.setSecure(true);
            response.addCookie(cookie);

            return foundUser;
        } else {
            //If the password doesn't match the one associated with the user
            throw new InvalidPasswordException();
        }

    }

    /**
     * Logs out the user and deletes their refresh and access token
     * @param request The Http request containing the refresh token
     * @param response The Http response without the jwt cookie
     * @return The User that was logged out
     * @throws IllegalArgumentException If the cookies don't exist, or they don't contain the jwt cookie
     */
    public User logout(HttpServletRequest request, HttpServletResponse response) {
        //TODO: on the client side delete the access token when they log out
        Cookie[] cookies = request.getCookies();

        //Gets the refresh token
        String refreshToken = RefreshTokenService.getRefreshTokenCookie(cookies);

        //Finds the user with that refresh token
        User foundUser = userRepository.findByRefreshToken(refreshToken);

        //Delete the cookie from the database and then clear the cookie from the response
        if (foundUser != null) {

            foundUser.setRefreshToken("");
            userRepository.save(foundUser);

        }

        //Removes the cookie by setting age to 0
        Cookie removeCookie = new Cookie("jwt", null);
        removeCookie.setHttpOnly(true);
        removeCookie.setSecure(true);
        removeCookie.setMaxAge(-1);
        response.addCookie(removeCookie);

        return foundUser;

    }

    /**
     * Gets the JWT token
     * @param user The user to encode
     * @param time The time that the token will expire
     * @return The token
     */
    public String getJWTToken(User user, Date time) {
        return generateJWT(user.getUsername(), user.getRoles(), time);
    }

    /**
     * Generates the JWT token with Auth0
     * @param username The username to encode
     * @param roles The list of roles to encode
     * @param time The time the token will expire
     * @return The token
     */
    private String generateJWT(String username, ArrayList<Integer> roles, Date time) {

        String jwtToken = JWT.create()
                //TODO: look more into this one and subject
                .withIssuer("MealMaker")
                .withSubject("MealMaker details")
                //Information in the token
                .withClaim("username", username)
                .withClaim("roles", roles.toString())
                //Date issued
                .withIssuedAt(new Date())
                //Expire time
                .withExpiresAt(time)
                .withJWTId(UUID.randomUUID().toString())
                //Token is valid instantly
                .withNotBefore(new Date())
                //Encodes the token
                .sign(algorithm);

        return jwtToken;
    }

    /**
     * Decodes the JWT token and returns the decoded object
     * @param jwtToken The token to decode
     * @return The decoded string
     */
    public DecodedJWT verifyJWT(String jwtToken) {

        //Verifies the token
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("MealMaker")
                .build();

        //Decodes the token
        DecodedJWT decodedJWT = verifier.verify(jwtToken);
        return decodedJWT;

    }

    public String hashPw(String pw) {
        return bCryptPasswordEncoder.encode(pw);
    }

}
