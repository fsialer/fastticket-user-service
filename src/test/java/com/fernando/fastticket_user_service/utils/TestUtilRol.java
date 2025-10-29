package com.fernando.fastticket_user_service.utils;

import com.fernando.fastticket_user_service.domain.models.Rol;
import com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.models.RolEntity;

public class TestUtilRol {
    public static RolEntity mockRolEntity(){
        return RolEntity.builder().id(1).code("USER").description("User").build();
    }

    public static Rol mockRol(){
        return Rol.builder().id(1).code("USER").description("User").build();
    }
}
