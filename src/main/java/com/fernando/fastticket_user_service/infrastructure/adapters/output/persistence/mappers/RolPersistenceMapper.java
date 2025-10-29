package com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.mappers;

import com.fernando.fastticket_user_service.domain.models.Rol;
import com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.models.RolEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RolPersistenceMapper {
    Rol rolEntityToRol(RolEntity rol);
}
