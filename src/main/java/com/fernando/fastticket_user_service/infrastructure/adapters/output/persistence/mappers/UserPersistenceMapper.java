package com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.mappers;

import com.fernando.fastticket_user_service.domain.models.User;
import com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.models.PersonEntity;
import com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.models.RolEntity;
import com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.models.UserEntity;
import org.mapstruct.Mapper;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserPersistenceMapper {
    User userEntityToUser(UserEntity userEntity);

    default UserEntity userToUserEntity(User user){
        return UserEntity.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRoles().stream()
                        .map(rol -> RolEntity.builder()
                                .id(rol.getId())
                                .code(rol.getCode())
                                .description(rol.getDescription())
                                .build())
                        .collect(Collectors.toSet()))
                .person(PersonEntity.builder().name(user.getName()).lastName(user.getLastName()).sex(user.getSex()).build())
                .build();
    }
}
