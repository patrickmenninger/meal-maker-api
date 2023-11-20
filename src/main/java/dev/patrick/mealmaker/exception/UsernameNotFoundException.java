package dev.patrick.mealmaker.exception;

/**
 * UsernameNotFoundException is an exception that is thrown if the username
 * used to log in to the website doesn't exist in the database.
 */
public class UsernameNotFoundException extends RuntimeException{

    /**
     * Creates an UsernameNotFoundException with the given message.
     * @param errMessage The message to attach to the exception
     */
    public UsernameNotFoundException(String errMessage) {

        super(errMessage);

    }

    /**
     * Creates an UsernameNotFoundException with the default message.
     */
    public UsernameNotFoundException() {

        this("Username does not exist.");

    }

}
