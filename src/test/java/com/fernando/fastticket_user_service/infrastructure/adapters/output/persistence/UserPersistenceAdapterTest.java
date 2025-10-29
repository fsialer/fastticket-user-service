package com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence;

import com.fernando.fastticket_user_service.domain.models.User;
import com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.mappers.UserPersistenceMapper;
import com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.models.UserEntity;
import com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.repositories.UserRepository;
import com.fernando.fastticket_user_service.utils.TestUtilUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserPersistenceAdapterTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserPersistenceMapper userPersistenceMapper;

    @InjectMocks
    private UserPersistenceAdapter userPersistenceAdapter;

    @Test
    @DisplayName("When Register An User Expect User Saved Correctly")
    void When_Register_An_User_Expect_User_Saved_Correctly() {
        // Arrange
        UserEntity userEntity = TestUtilUser.mockUserEntity();
        User user = TestUtilUser.mockUser();
        when(userPersistenceMapper.userToUserEntity(user)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userPersistenceMapper.userEntityToUser(userEntity)).thenReturn(user);
        
        // Act
        User userResult = userPersistenceAdapter.registerUser(user);
        
        // Assert
        assertEquals(user, userResult);
        verify(userPersistenceMapper, times(1)).userToUserEntity(user);
        verify(userRepository, times(1)).save(userEntity);
        verify(userPersistenceMapper, times(1)).userEntityToUser(userEntity);
    }

    @Test
    @DisplayName("When Exists By Email Expect Return True")
    void When_ExistsByEmail_Expect_ReturnTrue() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(Boolean.TRUE);
        
        // Act
        Boolean existsEmail = userPersistenceAdapter.existsByEmail("john@example.com");
        
        // Assert
        assertTrue(existsEmail);
        verify(userRepository, times(1)).existsByEmail(anyString());
    }

    @Test
    @DisplayName("When Not Exists By Email Expect Return False")
    void When_NotExistsByEmail_Expect_ReturnFalse() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(Boolean.FALSE);
        
        // Act
        Boolean existsEmail = userPersistenceAdapter.existsByEmail("john@example.com");
        
        // Assert
        assertFalse(existsEmail);
        verify(userRepository, times(1)).existsByEmail(anyString());
    }
}
