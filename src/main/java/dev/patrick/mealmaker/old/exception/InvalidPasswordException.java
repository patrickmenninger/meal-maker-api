package dev.patrick.mealmaker.old.exception;

/**
 * InvalidPasswordException is an exception that is thrown if the
 * inputted password is invalid for the given user.
 */
public class InvalidPasswordException extends RuntimeException {

    /**
     * Creates an InvalidPasswordException with the given message.
     * @param errMessage The message to attach to the exception
     */
    public InvalidPasswordException(String errMessage) {

        super(errMessage);

    }

    /**
     * Creates an InvalidPasswordException with the default message.
     */
    public InvalidPasswordException() {

        this("Invalid password");

    }

}
