package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.UserRequest;
import co.com.pragma.api.dto.UserResponse;
import co.com.pragma.model.user.User;

public class UserMapper {

    private UserMapper() {
        // Evita instanciación
    }

    public static User toDomain(UserRequest request) {
        return User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .birthDate(request.birthDate())
                .address(request.address())
                .phoneNumber(request.phoneNumber())
                .email(request.email())
                .rolId(request.rolId())
                .baseSalary(request.baseSalary())
                .build();
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getFirstName(),
                user.getLastName(),
                user.getBirthDate(),
                user.getAddress(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getRolId(),
                user.getBaseSalary()
        );
    }
}
