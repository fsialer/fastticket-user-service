package com.fernando.fastticket_user_service.utils;

import com.fernando.fastticket_user_service.domain.models.Rol;
import com.fernando.fastticket_user_service.domain.models.User;
import com.fernando.fastticket_user_service.infrastructure.adapters.input.models.reponses.UserRegisterResponse;
import com.fernando.fastticket_user_service.infrastructure.adapters.input.models.reponses.UserRoleResponse;
import com.fernando.fastticket_user_service.infrastructure.adapters.input.models.requests.AuthRequest;
import com.fernando.fastticket_user_service.infrastructure.adapters.input.models.requests.UserRequest;
import com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.models.PersonEntity;
import com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.models.RolEntity;
import com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.models.UserEntity;

import java.util.List;
import java.util.Set;

public class TestUtilUser {
    public static User mockUser(){
        return new User(
                "example@hotmail.com",
                "123456",
                Set.of(Rol.builder().code("user").description("User").build()),
                "Johnn",
                "Doeee",
                "M"
        );
    }

    public static UserEntity mockUserEntity(){
        return UserEntity.builder()
                .id(1L)
                .email("example@hotmail.com")
                .password("123456")
                .roles(Set.of(RolEntity.builder().code("user").description("User").build()))
                .person(PersonEntity.builder().name("Johnn").lastName("Doeee").sex("M").build())
                .build();
    }

    public static UserRegisterResponse mockUserResponse(){
        return UserRegisterResponse.builder()
                .id(1L)
                .email("example@hotmail.com")
                .build();
    }

    public static UserRequest mockUserRequest(){
        return UserRequest.builder()
                .email("example@hotmail.com")
                .password("123456")
                .name("Johnn")
                .lastName("Doeee")
                .sex("M")
                .build();
    }

    public static AuthRequest mockAuthRequest(){
        return AuthRequest.builder()
                .email("example@hotmail.com")
                .password("ollssds")
                .build();
    }

    public static UserRoleResponse mockUserRoleResponse(){
        return UserRoleResponse.builder()
                .id(1L)
                .email("example@hotmail.com")
                .fullName("John Doe")
                .roles(List.of("USER"))
                .build();
    }
}
