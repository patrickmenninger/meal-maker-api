package dev.patrick.mealmaker.exception;

public class UsernameNotFoundException extends RuntimeException{

    public UsernameNotFoundException(String errMessage) {

        super(errMessage);

    }

    public UsernameNotFoundException() {

        this("Username does not exist.");

    }

}
