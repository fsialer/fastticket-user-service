package com.fernando.fastticket_user_service.application.ports.output;

import com.fernando.fastticket_user_service.domain.models.Rol;

public interface RolPersistencePort {
    Rol findByCode(String code);
}
