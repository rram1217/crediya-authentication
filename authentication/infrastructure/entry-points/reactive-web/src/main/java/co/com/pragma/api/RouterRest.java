package co.com.pragma.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/users",
                    method = RequestMethod.GET,
                    beanClass = UserHandler.class,
                    beanMethod = "getAllUsers",
                    operation = @Operation(
                            operationId = "getAllUsers",
                            summary = "Obtener todos los usuarios",
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "OK"),
                                    @ApiResponse(responseCode = "500", description = "Error interno")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/users",
                    method = RequestMethod.POST,
                    beanClass = UserHandler.class,
                    beanMethod = "saveUser",
                    operation = @Operation(
                            operationId = "saveUser",
                            summary = "Crear usuarios",
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "OK"),
                                    @ApiResponse(responseCode = "500", description = "Error interno")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(UserHandler handler) {
        return route()
                .GET("/api/users", handler::getAllUsers)
                .POST("/api/users", handler::saveUser)
                .build();
    }
}
