package co.com.pragma.api;

import co.com.pragma.api.dto.UserRequest;
import co.com.pragma.api.mapper.UserMapper;
import co.com.pragma.model.exceptions.InvalidUserDataException;
import co.com.pragma.model.user.User;
import co.com.pragma.usecase.user.UserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserHandler {
    private final UserUseCase userUseCase;
    private final Validator validator;

    @Operation(
            summary = "Crear un usuario",
            description = "Guarda un usuario en la base de datos"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Usuario creado correctamente",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = User.class),
                    examples = @ExampleObject(value = """
                            {
                              "id": "123",
                              "firstName": "Juan",
                              "lastName": "Pérez",
                              "birthDate": "1990-05-12",
                              "address": "Calle 123",
                              "phoneNumber": "3001234567",
                              "email": "juan.perez@mail.com",
                              "rolId": 1,
                              "baseSalary": 2500000
                            }
                            """)
            )
    )
    @RequestBody(
            description = "Datos del usuario a crear",
            required = true,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserRequest.class),
                    examples = @ExampleObject(value = """
                            {
                              "firstName": "Juan",
                              "lastName": "Pérez",
                              "birthDate": "1990-05-12",
                              "address": "Calle 123",
                              "phoneNumber": "3001234567",
                              "email": "juan.perez@mail.com",
                              "rolId": 1,
                              "baseSalary": 2500000
                            }
                            """)
            )
    )
    public Mono<ServerResponse> saveUser(ServerRequest request) {
        return request.bodyToMono(UserRequest.class)
                .flatMap(this::validateReactive)
                .flatMap(userRequest -> userUseCase.saveUser(UserMapper.toDomain(userRequest)))
                .flatMap(user -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(user));
    }

    @Operation(
            summary = "Obtener todos los usuarios",
            description = "Retorna la lista completa de usuarios"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista obtenida correctamente",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = User.class),
                    examples = @ExampleObject(value = """
                            [
                              {
                                "id": "123",
                                "firstName": "Juan",
                                "lastName": "Pérez",
                                "birthDate": "1990-05-12",
                                "address": "Calle 123",
                                "phoneNumber": "3001234567",
                                "email": "juan.perez@mail.com",
                                "rolId": 1,
                                "baseSalary": 2500000
                              },
                              {
                                "id": "124",
                                "firstName": "Ana",
                                "lastName": "López",
                                "birthDate": "1992-08-21",
                                "address": "Carrera 45",
                                "phoneNumber": "3107654321",
                                "email": "ana.lopez@mail.com",
                                "rolId": 2,
                                "baseSalary": 3000000
                              }
                            ]
                            """)
            )
    )
    public Mono<ServerResponse> getAllUsers(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Objects.requireNonNullElseGet(userUseCase.getAllUsers(), Flux::empty), User.class);
    }

    private <T> Mono<T> validateReactive(T request) {
        var violations = validator.validate(request);

        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .reduce((msg1, msg2) -> msg1 + ", " + msg2)
                    .orElse("Datos inválidos");

            return Mono.error(new InvalidUserDataException(errorMessage));
        }

        return Mono.just(request);
    }

}
