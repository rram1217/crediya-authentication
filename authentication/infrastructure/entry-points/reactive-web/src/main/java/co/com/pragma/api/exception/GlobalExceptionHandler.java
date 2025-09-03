package co.com.pragma.api.exception;

import co.com.pragma.model.exceptions.UserNotFoundException;
import co.com.pragma.model.exceptions.InvalidUserDataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

import static co.com.pragma.api.exception.ErrorMessages.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        log.warn("User not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        Instant.now().toString(),
                        HttpStatus.NOT_FOUND.value(),
                        CODE_USER_NOT_FOUND,
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(InvalidUserDataException.class)
    public ResponseEntity<ErrorResponse> handleInvalidUserData(InvalidUserDataException ex) {
        log.warn("Invalid user data: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        Instant.now().toString(),
                        HttpStatus.BAD_REQUEST.value(),
                        CODE_INVALID_USER_DATA,
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        Instant.now().toString(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        CODE_INTERNAL_ERROR,
                        "Unexpected error occurred"
                ));
    }
}
