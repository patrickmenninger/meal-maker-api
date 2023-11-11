package dev.patrick.mealmaker.exception;

public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException(String errMessage) {

        super(errMessage);

    }

    public InvalidPasswordException() {

        this("Invalid password");

    }

}
