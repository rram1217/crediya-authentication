package co.com.pragma.model.user.validation;

public final class UserValidationMessages {

    private UserValidationMessages() {}

    public static final String FIRST_NAME_REQUIRED = "El campo firstName no puede ser nulo o vacío";
    public static final String LAST_NAME_REQUIRED = "El campo lastName no puede ser nulo o vacío";
    public static final String EMAIL_REQUIRED = "El campo email no puede ser nulo o vacío";
    public static final String EMAIL_INVALID = "El formato del email no es válido";
    public static final String BASE_SALARY_REQUIRED = "El campo baseSalary no puede ser nulo";
    public static final String BASE_SALARY_NEGATIVE = "El baseSalary no puede ser menor a 0";
    public static final String BASE_SALARY_EXCEEDS_LIMIT = "El baseSalary no puede ser mayor a 15000000";
}
