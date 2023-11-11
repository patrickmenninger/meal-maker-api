package dev.patrick.mealmaker.service;

import dev.patrick.mealmaker.exception.InvalidRefreshToken;
import dev.patrick.mealmaker.user.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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


    public void getAccessToken(HttpServletRequest req) throws MissingRequestCookieException {

        Cookie[] cookies = req.getCookies();
        String refreshToken = getRefreshTokenCookie(cookies);

        User foundUser = userService.findUserByRefreshToken(refreshToken);
        if (foundUser == null) {
            throw new InvalidRefreshToken();
        }


    }

    private String getRefreshTokenCookie(Cookie[] cookies) throws MissingRequestCookieException {

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
