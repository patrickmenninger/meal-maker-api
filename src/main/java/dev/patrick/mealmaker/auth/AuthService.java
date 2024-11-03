package dev.patrick.mealmaker.auth;

import dev.patrick.mealmaker.security.JwtService;
import dev.patrick.mealmaker.user.Role;
import dev.patrick.mealmaker.user.User;
import dev.patrick.mealmaker.user.UserRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        String jwtToken = jwtService.generateAccessToken(user);
        return AuthResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

    public AuthResponse authenticate(AuthRequest request, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        String refreshToken = jwtService.generateRefreshToken(user);
        Cookie jwtCookie = new Cookie("jwt", refreshToken);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        //TODO: Maybe expose the time from the jwtService class
        jwtCookie.setMaxAge(1000 * 60 * 60 * 24);
        response.addCookie(jwtCookie);

        String jwtToken = jwtService.generateAccessToken(user);
        return AuthResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("jwt", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setMaxAge(0);

        response.addCookie(jwtCookie);

    }

    public AuthResponse refreshToken(String refreshToken) {

        final String email;

        if (refreshToken == null) {
            throw new BadCredentialsException("Refresh token cookie does not exist");
        }

        email = jwtService.extractEmail(refreshToken);
        if (email != null) {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (jwtService.isTokenValid(refreshToken, user)) {
                String accessToken = jwtService.generateAccessToken(user);
                return AuthResponse.builder()
                        .accessToken(accessToken)
                        .build();
            }
        }

        throw new BadCredentialsException("Refresh token is invalid");

    }

}
