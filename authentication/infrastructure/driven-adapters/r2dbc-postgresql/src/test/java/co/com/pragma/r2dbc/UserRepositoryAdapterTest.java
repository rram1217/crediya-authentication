package co.com.pragma.r2dbc;

import co.com.pragma.model.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryAdapterTest {

    @InjectMocks
    UserRepositoryAdapter repositoryAdapter;

    @Mock
    UserRepository repository;

    @Mock
    ObjectMapper mapper;

    @Test
    void mustFindValueById() {
        UserEntity entity = new UserEntity();
        User user = User.builder()
                .firstName("Juan")
                .lastName("Pérez")
                .birthDate(LocalDate.now())
                .address("Calle 123")
                .phoneNumber("3001234567")
                .email("juan@test.com")
                .rolId(1L)
                .baseSalary(BigDecimal.valueOf(1200000))
                .build();


        when(repository.findById(1L)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, User.class)).thenReturn(user);

        Mono<User> result = repositoryAdapter.findById(1L);

        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void mustFindAllValues() {
        UserEntity entity = new UserEntity();
        User user = User.builder()
                .firstName("Juan")
                .lastName("Pérez")
                .birthDate(LocalDate.now())
                .address("Calle 123")
                .phoneNumber("3001234567")
                .email("juan@test.com")
                .rolId(1L)
                .baseSalary(BigDecimal.valueOf(1200000))
                .build();


        when(repository.findAll()).thenReturn(Flux.just(entity));
        when(mapper.map(entity, User.class)).thenReturn(user);

        Flux<User> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void mustFindByExample() {
        UserEntity entity = new UserEntity();
        User user = User.builder()
                .firstName("Juan")
                .lastName("Pérez")
                .birthDate(LocalDate.now())
                .address("Calle 123")
                .phoneNumber("3001234567")
                .email("juan@test.com")
                .rolId(1L)
                .baseSalary(BigDecimal.valueOf(1200000))
                .build();


        // 1️⃣ dominio -> entidad
        when(mapper.map(user, UserEntity.class)).thenReturn(entity);
        // 2️⃣ repo devuelve la entidad
        when(repository.findAll(any(Example.class))).thenReturn(Flux.just(entity));
        // 3️⃣ entidad -> dominio
        when(mapper.map(entity, User.class)).thenReturn(user);

        Flux<User> result = repositoryAdapter.findByExample(user);

        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void mustSaveValue() {
        UserEntity entity = new UserEntity();

        User user = User.builder()
                .firstName("Juan")
                .lastName("Pérez")
                .birthDate(LocalDate.now())
                .address("Calle 123")
                .phoneNumber("3001234567")
                .email("juan@test.com")
                .rolId(1L)
                .baseSalary(BigDecimal.valueOf(1200000))
                .build();

        when(mapper.map(user, UserEntity.class)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, User.class)).thenReturn(user);

        Mono<User> result = repositoryAdapter.save(user);

        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();
    }
}
