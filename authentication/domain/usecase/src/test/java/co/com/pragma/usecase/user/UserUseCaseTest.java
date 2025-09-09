package co.com.pragma.usecase.user;

import co.com.pragma.model.exceptions.InvalidUserDataException;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserUseCase Tests")
class UserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    private UserUseCase userUseCase;

    @BeforeEach
    void setUp() {
        userUseCase = new UserUseCase(userRepository);
    }

    @Nested
    @DisplayName("SaveUser Tests")
    class SaveUserTests {

        @Test
        @DisplayName("Should save user successfully when email doesn't exist")
        void shouldSaveUserSuccessfullyWhenEmailDoesntExist() {
            // Given
            User newUser = User.builder()
                    .firstName("Rafael")
                    .lastName("Alvarez")
                    .email("rafael@pragma.com")
                    .baseSalary(new BigDecimal("5000000"))
                    .birthDate(LocalDate.of(1990, 5, 15))
                    .rolId(1L)
                    .build();

            // Mock: getUserByEmail retorna vacío (usuario no existe)
            when(userRepository.getUserByEmail(anyString())).thenReturn(Mono.empty());
//            // Mock: saveUser retorna el usuario guardado
            when(userRepository.saveUser(any(User.class))).thenReturn(Mono.just(newUser));
//
//            // When
            Mono<User> result = userUseCase.saveUser(newUser);

            // Then
            StepVerifier.create(result)
                    .expectNext(newUser)
                    .verifyComplete();

            // Verify interactions
            verify(userRepository).getUserByEmail("rafael@pragma.com");
            verify(userRepository).saveUser(newUser);
        }

        @Test
        @DisplayName("Should verify error message when email already exists")
        void shouldVerifyErrorMessageWhenEmailAlreadyExists() {

            User existingUser = User.builder()
                    .firstName("Another")
                    .lastName("User")
                    .email("duplicate@pragma.com")
                    .baseSalary(new BigDecimal("4000000"))
                    .build();

            when(userRepository.getUserByEmail(existingUser.getEmail())).thenReturn(Mono.just(existingUser));

            // When
            Mono<User> result = userUseCase.saveUser(existingUser);

            // Then
            StepVerifier.create(result)
                    .expectErrorMatches(throwable ->
                            throwable instanceof InvalidUserDataException &&
                                    throwable.getMessage().equals("El correo ya está registrado")
                    )
                    .verify();

            verify(userRepository, never()).saveUser(any());
        }

        @Test
        @DisplayName("Should handle repository error during email check")
        void shouldHandleRepositoryErrorDuringEmailCheck() {
            // Given
            User newUser = User.builder()
                    .firstName("Rafael")
                    .lastName("Alvarez")
                    .email("test@pragma.com")
                    .baseSalary(new BigDecimal("5000000"))
                    .build();

            // Mock: getUserByEmail lanza error de base de datos
            when(userRepository.getUserByEmail(anyString()))
                    .thenReturn(Mono.error(new RuntimeException("Database connection error")));

            // When
            Mono<User> result = userUseCase.saveUser(newUser);

            // Then
            StepVerifier.create(result)
                    .expectError(RuntimeException.class)
                    .verify();

            verify(userRepository).getUserByEmail("test@pragma.com");
            verify(userRepository, never()).saveUser(any());
        }

        @Test
        @DisplayName("Should handle repository error during save")
        void shouldHandleRepositoryErrorDuringSave() {
            // Given
            User newUser = User.builder()
                    .firstName("Rafael")
                    .lastName("Alvarez")
                    .email("save-error@pragma.com")
                    .baseSalary(new BigDecimal("5000000"))
                    .build();

            // Mock: getUserByEmail retorna vacío (usuario no existe)
            when(userRepository.getUserByEmail(anyString())).thenReturn(Mono.empty());
            // Mock: saveUser lanza error
            when(userRepository.saveUser(any(User.class)))
                    .thenReturn(Mono.error(new RuntimeException("Save operation failed")));

            // When
            Mono<User> result = userUseCase.saveUser(newUser);

            // Then
            StepVerifier.create(result)
                    .expectError(RuntimeException.class)
                    .verify();

            verify(userRepository).getUserByEmail("save-error@pragma.com");
            verify(userRepository).saveUser(newUser);
        }
    }

    @Nested
    @DisplayName("GetAllUsers Tests")
    class GetAllUsersTests {

        @Test
        @DisplayName("Should return all users when users exist")
        void shouldReturnAllUsersWhenUsersExist() {
            // Given
            User user1 = User.builder()
                    .firstName("Rafael")
                    .lastName("Alvarez")
                    .email("rafael@pragma.com")
                    .baseSalary(new BigDecimal("5000000"))
                    .build();

            User user2 = User.builder()
                    .firstName("Ana")
                    .lastName("Garcia")
                    .email("ana@pragma.com")
                    .baseSalary(new BigDecimal("4000000"))
                    .build();

            User user3 = User.builder()
                    .firstName("Carlos")
                    .lastName("Lopez")
                    .email("carlos@pragma.com")
                    .baseSalary(new BigDecimal("6000000"))
                    .build();

            // Mock: getAllUsers retorna flux con usuarios
            when(userRepository.getAllUsers()).thenReturn(Flux.just(user1, user2, user3));

            // When
            Flux<User> result = userUseCase.getAllUsers();

            // Then
            StepVerifier.create(result)
                    .expectNext(user1)
                    .expectNext(user2)
                    .expectNext(user3)
                    .verifyComplete();

            verify(userRepository).getAllUsers();
        }

        @Test
        @DisplayName("Should return empty flux when no users exist")
        void shouldReturnEmptyFluxWhenNoUsersExist() {
            // Given
            when(userRepository.getAllUsers()).thenReturn(Flux.empty());

            // When
            Flux<User> result = userUseCase.getAllUsers();

            // Then
            StepVerifier.create(result)
                    .verifyComplete();

            verify(userRepository).getAllUsers();
        }

        @Test
        @DisplayName("Should handle repository error when getting all users")
        void shouldHandleRepositoryErrorWhenGettingAllUsers() {
            // Given
            when(userRepository.getAllUsers())
                    .thenReturn(Flux.error(new RuntimeException("Database connection error")));

            // When
            Flux<User> result = userUseCase.getAllUsers();

            // Then
            StepVerifier.create(result)
                    .expectError(RuntimeException.class)
                    .verify();

            verify(userRepository).getAllUsers();
        }

        @Test
        @DisplayName("Should return users in the same order as repository")
        void shouldReturnUsersInSameOrderAsRepository() {
            // Given
            User firstUser = User.builder()
                    .firstName("Alpha")
                    .lastName("User")
                    .email("alpha@pragma.com")
                    .baseSalary(new BigDecimal("3000000"))
                    .build();

            User secondUser = User.builder()
                    .firstName("Beta")
                    .lastName("User")
                    .email("beta@pragma.com")
                    .baseSalary(new BigDecimal("4000000"))
                    .build();

            when(userRepository.getAllUsers()).thenReturn(Flux.just(firstUser, secondUser));

            // When
            Flux<User> result = userUseCase.getAllUsers();

            // Then
            StepVerifier.create(result)
                    .expectNext(firstUser)
                    .expectNext(secondUser)
                    .verifyComplete();
        }

        @Test
        @DisplayName("Should handle large number of users efficiently")
        void shouldHandleLargeNumberOfUsersEfficiently() {
            // Given
            User[] users = new User[1000];
            for (int i = 0; i < 1000; i++) {
                users[i] = User.builder()
                        .firstName("User" + i)
                        .lastName("Test" + i)
                        .email("user" + i + "@pragma.com")
                        .baseSalary(new BigDecimal("3000000"))
                        .build();
            }

            when(userRepository.getAllUsers()).thenReturn(Flux.fromArray(users));

            // When
            Flux<User> result = userUseCase.getAllUsers();

            // Then
            StepVerifier.create(result)
                    .expectNextCount(1000)
                    .verifyComplete();

            verify(userRepository).getAllUsers();
        }
    }

    @Nested
    @DisplayName("Integration Scenarios")
    class IntegrationScenarios {

        @Test
        @DisplayName("Should save multiple users with different emails")
        void shouldSaveMultipleUsersWithDifferentEmails() {
            // Given
            User user1 = User.builder()
                    .firstName("User1")
                    .lastName("Test")
                    .email("user1@pragma.com")
                    .baseSalary(new BigDecimal("3000000"))
                    .build();

            User user2 = User.builder()
                    .firstName("User2")
                    .lastName("Test")
                    .email("user2@pragma.com")
                    .baseSalary(new BigDecimal("4000000"))
                    .build();

            // Mock para user1
            when(userRepository.getUserByEmail("user1@pragma.com")).thenReturn(Mono.empty());
            when(userRepository.saveUser(user1)).thenReturn(Mono.just(user1));

            // Mock para user2
            when(userRepository.getUserByEmail("user2@pragma.com")).thenReturn(Mono.empty());
            when(userRepository.saveUser(user2)).thenReturn(Mono.just(user2));

            // When & Then
            StepVerifier.create(userUseCase.saveUser(user1))
                    .expectNext(user1)
                    .verifyComplete();

            StepVerifier.create(userUseCase.saveUser(user2))
                    .expectNext(user2)
                    .verifyComplete();

            verify(userRepository).getUserByEmail("user1@pragma.com");
            verify(userRepository).saveUser(user1);
            verify(userRepository).getUserByEmail("user2@pragma.com");
            verify(userRepository).saveUser(user2);
        }

        @Test
        @DisplayName("Should maintain reactive stream properties")
        void shouldMaintainReactiveStreamProperties() {
            // Given
            User user = User.builder()
                    .firstName("Reactive")
                    .lastName("User")
                    .email("reactive@pragma.com")
                    .baseSalary(new BigDecimal("5000000"))
                    .build();

            when(userRepository.getUserByEmail(anyString())).thenReturn(Mono.empty());
            when(userRepository.saveUser(any(User.class))).thenReturn(Mono.just(user));
//
//            // When
            Mono<User> result = userUseCase.saveUser(user);
//
//            // Then - Verificar que es lazy (no se ejecuta hasta suscribirse)
//            verifyNoInteractions(userRepository);

            // Al suscribirse, debe ejecutarse
            StepVerifier.create(result)
                    .expectNext(user)
                    .verifyComplete();

            verify(userRepository).getUserByEmail("reactive@pragma.com");
            verify(userRepository).saveUser(user);
        }
    }
}