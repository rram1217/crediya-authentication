package co.com.pragma.r2dbc;

import co.com.pragma.model.user.User;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class UserRepositoryAdapter extends ReactiveAdapterOperations<
    User,
    UserEntity,
    Long,
    UserRepository
> implements co.com.pragma.model.user.gateways.UserRepository {

    private final TransactionalOperator transactionalOperator;
    private final ObjectMapper mapper;

    public UserRepositoryAdapter(UserRepository repository, ObjectMapper mapper, TransactionalOperator transactionalOperator) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper,d -> mapper.map(d, User.class));
        this.transactionalOperator = transactionalOperator;
        this.mapper = mapper;
    }

    @Override
    public Mono<User> saveUser(User user) {
        log.info("Iniciando guardado de usuario con Nombre: {}", user.getFirstName());
        return super.save(user)
                .doOnSuccess(u -> log.info("Usuario guardado exitosamente: {}", u))
                .doOnError(e -> log.error("Error al guardar usuario con Nombre: {}", user.getFirstName(), e))
                .as(transactionalOperator::transactional);
    }

    @Override
    public Flux<User> getAllUsers() {
        return super.findAll()
                .doOnComplete(() -> log.info("Consulta de todos los usuarios completada"))
                .doOnError(e -> log.error("Error consultando todos los usuarios", e));
    }

    @Override
    public Mono<User> getUserByEmail(String email) {
        return repository.findByEmail(email)
                .map(entity -> mapper.map(entity, User.class)) // convierte Entity → Domain
                .doOnNext(u -> log.info("Usuario encontrado con email {}: {}", email, u))
                .switchIfEmpty(Mono.empty()); // si no hay resultado
    }

}
