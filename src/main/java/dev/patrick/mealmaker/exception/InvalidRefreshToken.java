package dev.patrick.mealmaker.exception;

public class InvalidRefreshToken extends RuntimeException {

    public InvalidRefreshToken(String errMessage) {

        super(errMessage);

    }

    public InvalidRefreshToken() {

        this("Invalid refresh token.");

    }

}
