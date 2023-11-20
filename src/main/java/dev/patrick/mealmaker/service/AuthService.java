package dev.patrick.mealmaker.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.patrick.mealmaker.exception.InvalidPasswordException;
import dev.patrick.mealmaker.exception.UsernameNotFoundException;
import dev.patrick.mealmaker.repository.UserRepository;
import dev.patrick.mealmaker.user.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

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

    /** The instance of UserService to find the users */
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
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username and password are required.");
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
            String accessToken = getJWTToken(foundUser.getUsername(), new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE));
            //Expires after 1 day
            String refreshToken = getJWTToken(foundUser.getUsername(), new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE));

            //Updates the User's fields and then the new information is saved to the database
            foundUser.setAccessToken(accessToken);
            foundUser.setRefreshToken(refreshToken);
            userRepository.save(foundUser);

            //Creates the cookie and then adds it to the response
            Cookie cookie = new Cookie("jwt", refreshToken);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(SECONDS_IN_DAY);
            response.addCookie(cookie);

            return foundUser;
        } else {
            //If the password doesn't match the one associated with the user
            throw new InvalidPasswordException();
        }

    }

    /**
     * Generates the JWT token
     * @param username The username to encode
     * @param time The time that the token will expire
     * @return The token
     */
    public String getJWTToken(String username, Date time) {
        return generateJWT(username, time);
    }

    /**
     * Generates the JWT token with Auth0
     * @param username The username to encode
     * @param time The time the token will expire
     * @return The token
     */
    private String generateJWT(String username, Date time) {

        String jwtToken = JWT.create()
                //TODO: look more into this one and subject
                .withIssuer("MealMaker")
                .withSubject("MealMaker details")
                //Information in the token
                //TODO: add another claim for the user roles once created
                .withClaim("username", username)
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
     * Decodes the JWT token and returns the username encoded inside
     * @param jwtToken The token to decode
     * @return The username that was encoded
     */
    public String verifyJWT(String jwtToken) {

        //Verifies the token
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("MealMaker")
                .build();

        //Decodes the token
        DecodedJWT decodedJWT = verifier.verify(jwtToken);
        return decodedJWT.getClaim("username").asString();

    }

    public String hashPw(String pw) {
        return bCryptPasswordEncoder.encode(pw);
    }

}
