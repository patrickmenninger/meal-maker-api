package dev.patrick.mealmaker.old.exception;

/**
 * InvalidRefreshToken is an exception thrown if the inputted refresh token
 * either doesn't match one in the database, there is a problem decoding the
 * username from the refresh token, or the decoded username doesn't equal
 * the username of the user associated with the refresh token.
 */
public class InvalidRefreshToken extends RuntimeException {

    /**
     * Creates an InvalidRefreshToken exception with the given message.
     * @param errMessage The message to attach to the exception
     */
    public InvalidRefreshToken(String errMessage) {

        super(errMessage);

    }

    /**
     * Creates an InvalidRefreshToken exception with the default message.
     */
    public InvalidRefreshToken() {

        this("Invalid refresh token.");

    }

}
