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
@Service
public class RefreshTokenService {

    @Autowired
    private UserService userService;


    public String getAccessToken(HttpServletRequest req) throws MissingRequestCookieException {

        Cookie[] cookies = req.getCookies();
        String refreshToken = getRefreshTokenCookie(cookies);

        User foundUser = userService.findUserByRefreshToken(refreshToken);
        if (foundUser == null) {
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
        if (foundUser.getUsername() != decodedUsername) {
            throw new InvalidRefreshToken();
        }

        String accessToken = userService.getJWTToken(decodedUsername, UserService.ACCESS_TOKEN_EXPIRE);

        return accessToken;

    }

    private String getRefreshTokenCookie(Cookie[] cookies) throws MissingRequestCookieException {

        if (cookies == null) {
            throw new IllegalArgumentException("No cookies");
        }

        //TODO: look more into predicate for Java
        Predicate<Cookie> findJwtToken = t -> t.getName().equals("jwt");

        if (!Arrays.stream(cookies).anyMatch(findJwtToken)) {
            throw new MissingRequestCookieException("jwt", null);
        }

        String token = Arrays.stream(cookies).
                filter(findJwtToken).
                findFirst()
                .map(c -> c.getValue())
                .get();

        return token;

    }
}
