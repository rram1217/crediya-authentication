package co.com.pragma.model.exceptions;

public class InvalidUserDataException extends RuntimeException {
    public InvalidUserDataException(String message) {
        super(message);
    }

    public InvalidUserDataException(String message, Throwable cause) {
        super(message, cause);
    }
}