package co.com.pragma.api;

import co.com.pragma.model.user.User;
import co.com.pragma.usecase.user.UserUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RouterRest.class, UserHandler.class})
@WebFluxTest
@DisplayName("Router REST Tests")
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UserUseCase userUseCase;

    @Test
    @DisplayName("Should get all users successfully")
    void shouldGetAllUsersSuccessfully() {
        // Given
        User user1 = createTestUser("Rafael", "Alvarez", "rafael@pragma.com");
        User user2 = createTestUser("Ana", "Garcia", "ana@pragma.com");

        when(userUseCase.getAllUsers())
                .thenReturn(Flux.just(user1, user2));

        // When & Then
        webTestClient.get()
                .uri("/api/users") // Ajusta la URI según tu RouterRest
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class)
                .hasSize(2);
    }

    @Test
    @DisplayName("Should save user successfully via POST")
    void shouldSaveUserSuccessfullyViaPost() {
        // Given
        User userToSave = createTestUser("Carlos", "Lopez", "carlos@pragma.com");
        User savedUser = createTestUser("Carlos", "Lopez", "carlos@pragma.com");

        when(userUseCase.saveUser(any(User.class)))
                .thenReturn(Mono.just(savedUser));

        // When & Then
        webTestClient.post()
                .uri("/api/users") // Ajusta la URI según tu RouterRest
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createUserRequest())
                .exchange()
                .expectStatus().isCreated() // Cambié a 201 Created
                .expectBody(User.class)
                .value(user -> {
                    assert user.getFirstName().equals("Carlos");
                    assert user.getLastName().equals("Lopez");
                    assert user.getEmail().equals("carlos@pragma.com");
                });
    }

    @Test
    @DisplayName("Should handle empty users list")
    void shouldHandleEmptyUsersList() {
        // Given
        when(userUseCase.getAllUsers())
                .thenReturn(Flux.empty());

        // When & Then
        webTestClient.get()
                .uri("/api/users")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class)
                .hasSize(0);
    }

    @Test
    @DisplayName("Should handle validation errors in POST")
    void shouldHandleValidationErrorsInPost() {
        // Given - User con datos inválidos
        when(userUseCase.saveUser(any(User.class)))
                .thenReturn(Mono.error(new IllegalArgumentException("El campo email no puede ser nulo o vacío")));

        // When & Then
        webTestClient.post()
                .uri("/api/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createInvalidUserRequest())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Should handle user already exists error")
    void shouldHandleUserAlreadyExistsError() {
        // Given
        when(userUseCase.saveUser(any(User.class)))
                .thenReturn(Mono.error(new RuntimeException("El correo ya está registrado")));

        // When & Then
        webTestClient.post()
                .uri("/api/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createUserRequest())
                .exchange()
                .expectStatus().is4xxClientError();
    }

    // Métodos helper para crear datos de prueba
    private User createTestUser(String firstName, String lastName, String email) {
        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .baseSalary(new BigDecimal("5000000"))
                .birthDate(LocalDate.of(1990, 1, 1))
                .rolId(1L)
                .build();
    }

    private Object createUserRequest() {
        // Ajusta según tu UserRequest DTO
        return """
            {
                "firstName": "Carlos",
                "lastName": "Lopez",
                "email": "carlos@pragma.com",
                "baseSalary": 5000000,
                "birthDate": "1990-01-01",
                "rolId": 1
            }
            """;
    }

    private Object createInvalidUserRequest() {
        // Usuario con email inválido
        return """
            {
                "firstName": "Invalid",
                "lastName": "User",
                "email": "invalid-email",
                "baseSalary": 5000000
            }
            """;
    }
}