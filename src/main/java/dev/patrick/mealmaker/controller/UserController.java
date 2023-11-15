package dev.patrick.mealmaker.controller;

import dev.patrick.mealmaker.exception.InvalidPasswordException;
import dev.patrick.mealmaker.exception.InvalidRefreshToken;
import dev.patrick.mealmaker.exception.UsernameNotFoundException;
import dev.patrick.mealmaker.service.UserService;
import dev.patrick.mealmaker.user.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> addUser(@RequestBody Map<String, String> payload) {

        ResponseEntity<String> response;
        String resBody;

        try {

            User currentUser = userService.addUser(payload.get("username"), payload.get("password"));
            resBody = "New user " + currentUser.getUsername() + " created.";
            response = new ResponseEntity<>(resBody, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            resBody = "Username is taken.";
            return new ResponseEntity<>(resBody, HttpStatus.CONFLICT);
        }


        return response;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> payload, HttpServletResponse response) {

        ResponseEntity<String> responseEntity;
        User foundUser;
        String resBody;

        try {

            foundUser = userService.login(payload.get("username"), payload.get("password"), response);
            resBody = foundUser.getAccessToken();
            responseEntity = new ResponseEntity<>(resBody, HttpStatus.OK);

        } catch (IllegalArgumentException e){
            resBody = "Username and password required.";
            return new ResponseEntity<>(resBody, HttpStatus.BAD_REQUEST);
        } catch (UsernameNotFoundException e) {
            resBody = "Username not found.";
            return new ResponseEntity<>(resBody, HttpStatus.NOT_FOUND);
        } catch (InvalidPasswordException e) {
            resBody = "Invalid password.";
            return new ResponseEntity<>(resBody, HttpStatus.UNAUTHORIZED);
        }

        return responseEntity;

    }

    //TODO: get rid of this, just for testing purposes
    @PostMapping("/deleteAll")
    public void deleteUsers() {
        userService.deleteUsers();
    }



}
