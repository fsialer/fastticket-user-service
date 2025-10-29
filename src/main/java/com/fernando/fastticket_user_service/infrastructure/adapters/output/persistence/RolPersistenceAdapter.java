package com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence;

import com.fernando.fastticket_user_service.application.ports.output.RolPersistencePort;
import com.fernando.fastticket_user_service.domain.models.Rol;
import com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.mappers.RolPersistenceMapper;
import com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.repositories.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RolPersistenceAdapter implements RolPersistencePort {
    private final RolRepository rolRepository;
    private final RolPersistenceMapper rolPersistenceMapper;

    @Override
    public Rol findByCode(String code) {
        return rolPersistenceMapper.rolEntityToRol(rolRepository.findByCode(code));
    }
}
