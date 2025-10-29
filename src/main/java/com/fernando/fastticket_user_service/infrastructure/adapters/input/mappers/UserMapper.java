package com.fernando.fastticket_user_service.infrastructure.adapters.input.mappers;

import com.fernando.fastticket_user_service.domain.models.Rol;
import com.fernando.fastticket_user_service.domain.models.User;
import com.fernando.fastticket_user_service.infrastructure.adapters.input.models.reponses.UserRegisterResponse;
import com.fernando.fastticket_user_service.infrastructure.adapters.input.models.requests.UserRequest;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserMapper {
    default UserRegisterResponse userResponseToUser(User user){
        return UserRegisterResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .build();
    }

    default User userRequestToUser(UserRequest rq){
        return new User(
                rq.getEmail(),
                rq.getPassword(),
                Set.of(Rol.builder().code("USER").build()),
                rq.getName(),
                rq.getLastName(),
                rq.getSex()
        );
    }
}
