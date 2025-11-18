package com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.mappers;

import com.fernando.fastticket_user_service.domain.factory.PersonFactory;
import com.fernando.fastticket_user_service.domain.models.Rol;
import com.fernando.fastticket_user_service.domain.models.User;
import com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.models.PersonEntity;
import com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.models.RolEntity;
import com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.models.UserEntity;
import org.mapstruct.Mapper;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserPersistenceMapper {
    default User userEntityToUser(UserEntity userEntity){
        return Optional.ofNullable(userEntity)
                .map(user->(User) PersonFactory.create(Map.of(
                            "id",userEntity.getId(),
                            "email",userEntity.getEmail(),
                            "password",userEntity.getPassword(),
                            "roles",userEntity.getRoles().stream()
                                    .map(rol-> new Rol(rol.getId(), rol.getCode(), rol.getDescription())).collect(Collectors.toSet()),
                            "confirmEmail",userEntity.getConfirmEmail(),
                            "name", userEntity.getPerson().getName(),
                            "lastName",userEntity.getPerson().getLastName(),
                            "sex",userEntity.getPerson().getSex()))
                )
                .orElse(null);
    }

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
                .confirmEmail(user.isConfirmEmail())
                .build();
    }
}
