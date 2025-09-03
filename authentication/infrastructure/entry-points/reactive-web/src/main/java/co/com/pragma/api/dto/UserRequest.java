package co.com.pragma.api.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record UserRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotNull LocalDate birthDate,
        @NotBlank String address,
        @NotBlank String phoneNumber,
        @Email @NotBlank String email,
        @NotNull Long rolId,
        @NotNull
        @Min(value = 0, message = "El salario base debe ser mayor o igual a 0")
        @Max(value = 15000000, message = "El salario base no puede superar 15.000.000")
        BigDecimal baseSalary
) {}
