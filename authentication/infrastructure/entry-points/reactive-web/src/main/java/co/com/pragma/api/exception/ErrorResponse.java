package co.com.pragma.api.exception;

public record ErrorResponse(
        String timestamp,
        int status,
        String code,
        String message
) {}
