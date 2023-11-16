package dev.patrick.mealmaker.controller;

import dev.patrick.mealmaker.exception.InvalidRefreshToken;
import dev.patrick.mealmaker.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/refresh")
public class RefreshTokenController {

    @Autowired
    RefreshTokenService refreshTokenService;

    @GetMapping
    public ResponseEntity<String> getAccessToken(HttpServletRequest req) {

        String resBody = null;

        try {
            resBody = refreshTokenService.getAccessToken(req);
        } catch (MissingRequestCookieException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (InvalidRefreshToken e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(resBody, HttpStatus.OK);

    }

}
