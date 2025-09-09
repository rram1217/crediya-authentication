package co.com.pragma.model.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static co.com.pragma.model.user.validation.UserValidationMessages.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests del Modelo User según Enunciado")
class UserTest {

    @Nested
    @DisplayName("Validaciones de Campos Requeridos")
    class ValidacionesCamposRequeridos {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "  ", "\t", "\n"})
        @DisplayName("Debe rechazar firstName nulos o vacíos")
        void debeRechazarfirstNameNulosOVacios(String firstName) {
            // When & Then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> User.builder()
                            .firstName(firstName)
                            .lastName("Alvarez")
                            .email("test@pragma.com")
                            .baseSalary(new BigDecimal("3000000"))
                            .build()
            );

            assertEquals(FIRST_NAME_REQUIRED, exception.getMessage());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "  ", "\t", "\n"})
        @DisplayName("Debe rechazar lastName nulos o vacíos")
        void debeRechazarlastNameNulosOVacios(String lastName) {
            // When & Then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> User.builder()
                            .firstName("Rafael")
                            .lastName(lastName)
                            .email("test@pragma.com")
                            .baseSalary(new BigDecimal("3000000"))
                            .build()
            );

            assertEquals(LAST_NAME_REQUIRED, exception.getMessage());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "  ", "\t", "\n"})
        @DisplayName("Debe rechazar email nulo o vacío")
        void debeRechazaremailElectronicoNuloOVacio(String email) {
            // When & Then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> User.builder()
                            .firstName("Rafael")
                            .lastName("Alvarez")
                            .email(email)
                            .baseSalary(new BigDecimal("3000000"))
                            .build()
            );

            assertEquals(EMAIL_REQUIRED, exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar baseSalary nulo")
        void debeRechazarbaseSalaryBaseNulo() {
            // When & Then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> User.builder()
                            .firstName("Rafael")
                            .lastName("Alvarez")
                            .email("test@pragma.com")
                            .baseSalary(null)
                            .build()
            );

            assertEquals(BASE_SALARY_REQUIRED, exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Validaciones de Formato de email Electrónico")
    class ValidacionesFormatoemail {

        @ParameterizedTest
        @ValueSource(strings = {
                "rafael@pragma.com",
                "test.user@pragma.com.co",
                "user+tag@domain.org",
                "123@test.co",
                "USER@DOMAIN.COM"
        })
        @DisplayName("Debe aceptar formatos válidos de email electrónico")
        void debeAceptarFormatosValidosDeemail(String emailValido) {
            // When & Then
            assertDoesNotThrow(() ->
                    User.builder()
                            .firstName("Rafael")
                            .lastName("Alvarez")
                            .email(emailValido)
                            .baseSalary(new BigDecimal("3000000"))
                            .build()
            );
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "email-invalido",
                "@pragma.com",
                "test@",
                "test..test@domain.com",
                "test@domain.",
                "test@domain@com"
        })
        @DisplayName("Debe rechazar formatos inválidos de email electrónico")
        void debeRechazarFormatosInvalidosDeemail(String emailInvalido) {
            // When & Then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> User.builder()
                            .firstName("Rafael")
                            .lastName("Alvarez")
                            .email(emailInvalido)
                            .baseSalary(new BigDecimal("3000000"))
                            .build()
            );

            assertEquals(EMAIL_INVALID, exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Validaciones de baseSalary Base")
    class ValidacionesbaseSalaryBase {

        @ParameterizedTest
        @ValueSource(strings = {
                "0",
                "1000000",
                "5000000",
                "10000000",
                "15000000"
        })
        @DisplayName("Debe aceptar baseSalarys válidos entre 0 y 15000000")
        void debeAceptarbaseSalarysValidosEnRango(String baseSalaryValido) {
            // When & Then
            assertDoesNotThrow(() ->
                    User.builder()
                            .firstName("Rafael")
                            .lastName("Alvarez")
                            .email("test@pragma.com")
                            .baseSalary(new BigDecimal(baseSalaryValido))
                            .build()
            );
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "-1",
                "-1000000",
                "15000001",
                "20000000",
                "50000000"
        })
        @DisplayName("Debe rechazar baseSalarys fuera del rango 0-15000000")
        void debeRechazarbaseSalarysFueraDeRango(String baseSalaryInvalido) {
            // When & Then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> User.builder()
                            .firstName("Rafael")
                            .lastName("Alvarez")
                            .email("test@pragma.com")
                            .baseSalary(new BigDecimal(baseSalaryInvalido))
                            .build()
            );
        }

        @Test
        @DisplayName("Debe rechazar baseSalary base negativo")
        void debeRechazarbaseSalaryBaseNegativo() {
            // When & Then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> User.builder()
                            .firstName("Rafael")
                            .lastName("Alvarez")
                            .email("test@pragma.com")
                            .baseSalary(new BigDecimal("-100"))
                            .build()
            );

            assertEquals(BASE_SALARY_NEGATIVE, exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar baseSalary base mayor a 15000000")
        void debeRechazarbaseSalaryBaseMayorA15Millones() {
            // When & Then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> User.builder()
                            .firstName("Rafael")
                            .lastName("Alvarez")
                            .email("test@pragma.com")
                            .baseSalary(new BigDecimal("15000001"))
                            .build()
            );

            assertEquals(BASE_SALARY_EXCEEDS_LIMIT, exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Creación Exitosa de User")
    class CreacionExitosaUser {

        @Test
        @DisplayName("Debe crear User con todos los datos válidos")
        void debeCrearUserConTodosLosDatosValidos() {
            // Given
            String firstName = "Rafael";
            String lastName = "Alvarez";
            String email = "rafael.alvarez@pragma.com";
            BigDecimal baseSalary = new BigDecimal("5000000");
            LocalDate birthDate = LocalDate.of(1990, 5, 15);
            String address = "Calle 123 #45-67, Cúcuta";
            String phoneNumber = "+57 3001234567";
            Long roleId = 1L;

            // When
            User user = User.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .baseSalary(baseSalary)
                    .birthDate(birthDate)
                    .address(address)
                    .phoneNumber(phoneNumber)
                    .rolId(roleId)
                    .build();

            // Then
            assertAll(
                    () -> assertEquals(firstName, user.getFirstName()),
                    () -> assertEquals(lastName, user.getLastName()),
                    () -> assertEquals(email.toLowerCase(), user.getEmail()),
                    () -> assertEquals(0, baseSalary.compareTo(user.getBaseSalary())),
                    () -> assertEquals(birthDate, user.getBirthDate()),
                    () -> assertEquals(address, user.getAddress()),
                    () -> assertEquals(phoneNumber, user.getPhoneNumber()),
                    () -> assertEquals(roleId, user.getRolId())
            );
        }

        @Test
        @DisplayName("Debe normalizar el email electrónico a minúsculas")
        void debeNormalizaremailElectronicoAMinusculas() {
            // Given
            String emailMayusculas = "RAFAEL.ALVAREZ@PRAGMA.COM";

            // When
            User user = User.builder()
                    .firstName("Rafael")
                    .lastName("Alvarez")
                    .email(emailMayusculas)
                    .baseSalary(new BigDecimal("3000000"))
                    .build();

            // Then
            assertEquals("rafael.alvarez@pragma.com", user.getEmail());
        }

        @Test
        @DisplayName("Debe trimmar espacios en firstName y lastName")
        void debeTrimmarEspaciosEnfirstNameYlastName() {
            // Given
            String firstNameConEspacios = "  Rafael  ";
            String lastNameConEspacios = "  Alvarez  ";

            // When
            User user = User.builder()
                    .firstName(firstNameConEspacios)
                    .lastName(lastNameConEspacios)
                    .email("test@pragma.com")
                    .baseSalary(new BigDecimal("3000000"))
                    .build();

            // Then
            assertEquals("Rafael", user.getFirstName());
            assertEquals("Alvarez", user.getLastName());
        }
    }
}