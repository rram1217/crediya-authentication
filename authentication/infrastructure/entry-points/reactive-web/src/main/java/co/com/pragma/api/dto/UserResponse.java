package co.com.pragma.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UserResponse(
        String firstName,
        String lastName,
        LocalDate birthDate,
        String address,
        String phoneNumber,
        String email,
        Long rolId,
        BigDecimal baseSalary
) {}
