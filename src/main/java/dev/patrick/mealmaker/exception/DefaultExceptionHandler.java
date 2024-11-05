package dev.patrick.mealmaker.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIError> handleException(ResourceNotFoundException e, HttpServletRequest request) {

        APIError apiError = new APIError(
            request.getRequestURI(),
            e.getMessage(),
            HttpStatus.NOT_FOUND.value(),
            LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler({ExpiredJwtException.class, BadCredentialsException.class, SignatureException.class})
    public ResponseEntity<APIError> handleException(Exception e, HttpServletRequest request) {

        APIError apiError = new APIError(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);

    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<APIError> handleAllExceptions(Exception e,
//                                                    HttpServletRequest request) {
//        APIError apiError = new APIError(
//                request.getRequestURI(),
//                e.getMessage(),
//                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                LocalDateTime.now()
//        );
//
//        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

}
