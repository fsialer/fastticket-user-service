package com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence;

import com.fernando.fastticket_user_service.application.ports.output.UserPersistencePort;
import com.fernando.fastticket_user_service.domain.models.User;
import com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.mappers.UserPersistenceMapper;
import com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserPersistencePort {
    private final UserRepository userRepository;
    private final UserPersistenceMapper userPersistenceMapper;
    @Override
    public User registerUser(User user) {
        return userPersistenceMapper.userEntityToUser(userRepository.save(userPersistenceMapper.userToUserEntity(user)));
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User getUserByEmail(String email) {
        return userPersistenceMapper.userEntityToUser(userRepository.findByEmail(email));
    }
}
