package co.com.pragma.api.exception;

public record ErrorResponse(
        String timestamp,
        String code,
        String message
) {}
