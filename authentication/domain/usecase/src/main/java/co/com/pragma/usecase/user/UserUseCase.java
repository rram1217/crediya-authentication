package co.com.pragma.usecase.user;

import co.com.pragma.model.exceptions.InvalidUserDataException;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository userRepository;

    public Mono<User> saveUser(User user){
        return userRepository.getUserByEmail(user.getEmail())
                .flatMap(existingUser -> Mono.<User>error(new InvalidUserDataException("El correo ya está registrado")))
                .switchIfEmpty(Mono.defer(() -> userRepository.saveUser(user)));
    }

    public Flux<User> getAllUsers(){
        return userRepository.getAllUsers();
    }

}
