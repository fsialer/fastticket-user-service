package com.fernando.fastticket_user_service.utils;

import com.fernando.fastticket_user_service.domain.models.Rol;
import com.fernando.fastticket_user_service.domain.models.User;
import com.fernando.fastticket_user_service.infrastructure.adapters.input.models.reponses.UserRegisterResponse;
import com.fernando.fastticket_user_service.infrastructure.adapters.input.models.requests.UserRequest;
import com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.models.PersonEntity;
import com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.models.RolEntity;
import com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.models.UserEntity;

import java.util.Set;

public class TestUtilUser {
    public static User mockUser(){
        return new User(
                "asialer05@hotmail.com",
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
                .email("asialer05@hotmail.com")
                .password("123456")
                .roles(Set.of(RolEntity.builder().code("user").description("User").build()))
                .person(PersonEntity.builder().name("Johnn").lastName("Doeee").sex("M").build())
                .build();
    }

    public static UserRegisterResponse mockUserResponse(){
        return UserRegisterResponse.builder()
                .id(1L)
                .email("asialer05@hotmail.com")
                .build();
    }

    public static UserRequest mockUserRequest(){
        return UserRequest.builder()
                .email("asialer05@hotmail.com")
                .password("123456")
                .name("Johnn")
                .lastName("Doeee")
                .sex("M")
                .build();
    }
}
