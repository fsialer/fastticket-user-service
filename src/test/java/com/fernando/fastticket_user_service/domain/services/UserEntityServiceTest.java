package com.fernando.fastticket_user_service.domain.services;

import com.fernando.fastticket_user_service.application.ports.output.RolPersistencePort;
import com.fernando.fastticket_user_service.application.ports.output.NotificationPort;
import com.fernando.fastticket_user_service.application.ports.output.UserPersistencePort;
import com.fernando.fastticket_user_service.domain.chainresponsibility.CheckEmailHandler;
import com.fernando.fastticket_user_service.domain.chainresponsibility.CheckRolHandler;
import com.fernando.fastticket_user_service.domain.chainresponsibility.EncoderPasswordHandler;
import com.fernando.fastticket_user_service.domain.chainresponsibility.UserRegistrationHandler;
import com.fernando.fastticket_user_service.domain.exceptions.RolNotFoundException;
import com.fernando.fastticket_user_service.domain.exceptions.UserEmailExistsException;
import com.fernando.fastticket_user_service.domain.models.Rol;
import com.fernando.fastticket_user_service.domain.models.User;
import com.fernando.fastticket_user_service.domain.decorator.SendEmailAfterRegistrationDecorator;
import com.fernando.fastticket_user_service.infrastructure.adapters.output.notification.facade.NotificationFacade;

import com.fernando.fastticket_user_service.utils.TestUtilRol;
import com.fernando.fastticket_user_service.utils.TestUtilUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserEntityServiceTest {

    @Mock
    private UserPersistencePort userPersistencePort;

    @Mock
    private RolPersistencePort rolPersistencePort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private NotificationPort notificationPort;

    @Mock
    private NotificationFacade notificationFacade;

    private UserService userService;

    private CheckEmailHandler checkEmailHandler;

    private CheckRolHandler checkRolHandler;

    private EncoderPasswordHandler encoderPasswordHandler;

    private SendEmailAfterRegistrationDecorator sendEmailAfterRegistrationDecorator;

    //private List<UserRegistrationHandler> handlers=new ArrayList<>();

    @BeforeEach
    void setUp() {
        checkEmailHandler = new CheckEmailHandler(userPersistencePort);
        checkRolHandler = new CheckRolHandler(rolPersistencePort);
        encoderPasswordHandler = new EncoderPasswordHandler(passwordEncoder);
        
         List<UserRegistrationHandler> handlers = Arrays.asList(
            checkEmailHandler, encoderPasswordHandler, checkRolHandler
        );
        userService = new UserService(userPersistencePort, handlers);
        sendEmailAfterRegistrationDecorator=new SendEmailAfterRegistrationDecorator(userService, notificationFacade);
    }

    @Test
    @DisplayName("When Register An User Expect User Saved Correctly")
    void When_RegisterAnUser_Expect_UserSavedCorrectly(){
        User user = TestUtilUser.mockUser();
        Rol rol = TestUtilRol.mockRol();
        
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(Boolean.FALSE);
        when(userPersistencePort.registerUser(any())).thenReturn(user);
        when(rolPersistencePort.findByCode(anyString())).thenReturn(rol);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        User userResponse = userService.registerUser(user);
        User userEmail=sendEmailAfterRegistrationDecorator.registerUser(userResponse);
        
        assertEquals(userEmail, user);
        verify(userPersistencePort, times(2)).existsByEmail(anyString());
        verify(passwordEncoder, times(2)).encode(anyString());
        verify(rolPersistencePort, times(2)).findByCode(anyString());
        verify(userPersistencePort, times(2)).registerUser(any());
    }

    @Test
    @DisplayName("Expect UserEmailExistsException When Email User Exists")
    void Expect_UserEmailExistsException_When_EmailUserExists(){
        User user = TestUtilUser.mockUser();
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(Boolean.TRUE);
        assertThrows(UserEmailExistsException.class, () -> sendEmailAfterRegistrationDecorator.registerUser(user));
        
        verify(userPersistencePort, times(1)).existsByEmail(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userPersistencePort, never()).registerUser(any());
    }

    @Test
    @DisplayName("Expect RolNotFoundException When Rol Do Not Exists")
    void Expect_RolNotFoundException_When_RolDoNotExists(){
        User user = TestUtilUser.mockUser();
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(Boolean.FALSE);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(rolPersistencePort.findByCode(anyString())).thenReturn(null);
        
        assertThrows(RolNotFoundException.class, () -> sendEmailAfterRegistrationDecorator.registerUser(user));
        
        verify(userPersistencePort, times(1)).existsByEmail(anyString());
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(rolPersistencePort, times(1)).findByCode(anyString());
        verify(userPersistencePort, never()).registerUser(any());
    }
}
