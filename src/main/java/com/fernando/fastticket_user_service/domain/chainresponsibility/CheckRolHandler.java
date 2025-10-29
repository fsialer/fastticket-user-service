package com.fernando.fastticket_user_service.domain.chainresponsibility;

import com.fernando.fastticket_user_service.application.ports.output.RolPersistencePort;
import com.fernando.fastticket_user_service.domain.exceptions.RolNotFoundException;
import com.fernando.fastticket_user_service.domain.models.Rol;
import com.fernando.fastticket_user_service.domain.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CheckRolHandler implements UserRegistrationHandler{
    private final RolPersistencePort rolPersistencePort;
    @Override
    public void handle(User user) {
        Set<Rol> roles = user.getRoles().stream()
                .map(roleDto -> {
                    Rol rol = rolPersistencePort.findByCode(roleDto.getCode());
                    if (rol == null) {
                        throw new RolNotFoundException("Rol don't exists: " + roleDto.getCode());
                    }
                    return rol;
                })
                .collect(Collectors.toSet());
        user.setRoles(new HashSet<>(roles));
    }
}
