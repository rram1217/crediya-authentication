package co.com.pragma.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "DTO que representa los datos para crear un usuario")
public record UserRequest(
        @NotNull @NotBlank @Schema(description = "Nombre del usuario", example = "Juan Pérez") String firstName,
        @NotNull @NotBlank String lastName,
        LocalDate birthDate,
        @NotBlank @NotNull String address,
        String phoneNumber,
        @Email @NotBlank @NotNull @Schema(description = "Correo electrónico del usuario", example = "juan.perez@mail.com") String email,
        @NotNull Long rolId,
        @NotNull
        @NotBlank
        @Min(value = 0, message = "El salario base debe ser mayor o igual a 0")
        @Max(value = 15000000, message = "El salario base no puede superar 15.000.000")
        BigDecimal baseSalary
) {}
