package co.com.pragma.api.exception;

public final class ErrorMessages {

    private ErrorMessages() {
        throw new IllegalStateException("Utility class");
    }

    // Códigos de error
    public static final String CODE_USER_NOT_FOUND = "USER_NOT_FOUND";
    public static final String CODE_INVALID_USER_DATA = "INVALID_USER_DATA";
    public static final String CODE_INTERNAL_ERROR = "INTERNAL_ERROR";

    // Mensajes de error
    public static final String MSG_USER_NOT_FOUND = "Usuario no encontrado";
    public static final String MSG_INVALID_USER_DATA = "Los datos del usuario son inválidos";
    public static final String MSG_INTERNAL_ERROR = "Ha ocurrido un error inesperado. Intente más tarde.";
}
