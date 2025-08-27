package co.com.pragma.r2dbc;

import co.com.pragma.model.user.User;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class UserRepositoryAdapter extends ReactiveAdapterOperations<
    User,
    UserEntity,
    Long,
    UserRepository
> implements co.com.pragma.model.user.gateways.UserRepository {
    public UserRepositoryAdapter(UserRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper,d -> mapper.map(d, User.class));
    }

    @Override
    public Mono<User> saveUser(User user) {
        super.save(user);
        return Mono.just(user);
    }

    @Override
    public Flux<User> getAllUsers() {
        return super.findAll();
    }

    @Override
    public Mono<User> getUserByIdNumber(Long idNumber) {
        return null;
    }

    @Override
    public Mono<User> editUser(User user) {
        return null;
    }

    @Override
    public Mono<Void> deleteUser(Long IdNumber) {
        return null;
    }
}
