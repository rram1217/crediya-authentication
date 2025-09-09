package co.com.pragma.model.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.regex.Pattern;

import static co.com.pragma.model.user.validation.UserValidationMessages.*;

@Getter
@Setter
public class User {
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;
    private String email;
    private Long rolId;
    private BigDecimal baseSalary;

        // Expresión regular para validar emails simples
    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    @Builder(toBuilder = true)
    public User(String firstName, String lastName, LocalDate birthDate,
                String address, String phoneNumber, String email,
                Long rolId, BigDecimal baseSalary) {

        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException(FIRST_NAME_REQUIRED);
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException(LAST_NAME_REQUIRED);
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException(EMAIL_REQUIRED);
        }
        if (baseSalary == null) {
            throw new IllegalArgumentException(BASE_SALARY_REQUIRED);
        }

        String normalizedEmail = email.trim().toLowerCase();

        // Validación de formato de email
        if (!EMAIL_REGEX.matcher(normalizedEmail).matches()
                || normalizedEmail.contains("..")
                || normalizedEmail.startsWith(".")
                || normalizedEmail.endsWith(".")) {
            throw new IllegalArgumentException(EMAIL_INVALID);
        }

        this.email = normalizedEmail;

        // Validación de rango de salario
        if (baseSalary.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(BASE_SALARY_NEGATIVE);
        }
        if (baseSalary.compareTo(new BigDecimal("15000000")) > 0) {
            throw new IllegalArgumentException(BASE_SALARY_EXCEEDS_LIMIT);
        }

        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.birthDate = birthDate;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email.trim().toLowerCase();
        this.rolId = rolId;
        this.baseSalary = baseSalary;
    }
}
