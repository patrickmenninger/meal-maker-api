package dev.patrick.mealmaker.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.patrick.mealmaker.exception.InvalidRefreshToken;
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
public class UserService {

    private static final int MILLISECONDS_IN_DAY = 86400000;
    private static final int SECONDS_IN_DAY = 86400;

    //TODO: figure out what key to use
    private Algorithm algorithm = Algorithm.HMAC256("secret");
    private static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);

    @Autowired
    UserRepository userRepository;

    public User addUser(String username, String password) {

        if (userRepository.findByUsername(username) != null) {
            throw new IllegalArgumentException();
        }

        String pw_hash = bCryptPasswordEncoder.encode(password);

        User user = userRepository.insert(new User(username, pw_hash));

        return user;

    }

    //TODO: add JWT token being added to database when the user logs in
    //The JWT methods are already made they just need to be changed a bit for parameters and maybe return statements
    public User login(String username, String password, HttpServletResponse response) {

        if (username == null || password == null) {
            throw new IllegalArgumentException("Username and password are required.");
        }

        User foundUser = userRepository.findByUsername(username);

        if (foundUser == null) {
            throw new UsernameNotFoundException(username + " does not exist.");
        }

        if (bCryptPasswordEncoder.matches(password, foundUser.getPassword())) {

            //Expires in 10 seconds
            String accessToken = generateJWT(foundUser.getUsername(), new Date(System.currentTimeMillis() + 10000));
            //Expires after 1 day
            String refreshToken = generateJWT(foundUser.getUsername(), new Date(System.currentTimeMillis() + MILLISECONDS_IN_DAY));

            foundUser.setAccessToken(accessToken);
            foundUser.setRefreshToken(refreshToken);
            userRepository.save(foundUser);

            Cookie cookie = new Cookie("jwt", refreshToken);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(SECONDS_IN_DAY);
            response.addCookie(new Cookie("jwt", refreshToken));

            return foundUser;
        } else {
            throw new InvalidRefreshToken();
        }

    }

    private String generateJWT(String username, Date time) {

        String jwtToken = JWT.create()
                .withIssuer("MealMaker")
                .withSubject("MealMaker details")
                .withClaim("username", username)
                .withIssuedAt(new Date())
                .withExpiresAt(time)
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date())
                .sign(algorithm);

        return jwtToken;
    }

    public String verifyJWT(String jwtToken) {

        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("MealMaker")
                .build();

        System.out.println(jwtToken);
        DecodedJWT decodedJWT = verifier.verify(jwtToken);
        return decodedJWT.getClaim("username").asString();

    }

    public User findUserByRefreshToken(String refreshToken) {
        return userRepository.findByRefreshToken(refreshToken);
    }

    public void deleteUsers() {
        userRepository.deleteAll();
    }

}
