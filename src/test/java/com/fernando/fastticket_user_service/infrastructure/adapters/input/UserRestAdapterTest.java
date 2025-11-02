package com.fernando.fastticket_user_service.infrastructure.adapters.input;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.fastticket_user_service.application.ports.input.CheckCredentialUseCase;
import com.fernando.fastticket_user_service.application.ports.input.RegisterUserUseCase;
import com.fernando.fastticket_user_service.domain.models.User;
import com.fernando.fastticket_user_service.infrastructure.adapters.input.mappers.UserMapper;
import com.fernando.fastticket_user_service.infrastructure.adapters.input.models.reponses.UserRegisterResponse;
import com.fernando.fastticket_user_service.infrastructure.adapters.input.models.reponses.UserRoleResponse;
import com.fernando.fastticket_user_service.infrastructure.adapters.input.models.requests.AuthRequest;
import com.fernando.fastticket_user_service.infrastructure.config.TestSecurityConfig;
import com.fernando.fastticket_user_service.utils.TestUtilUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestSecurityConfig.class)
@WebMvcTest(UserRestAdapter.class)
class UserRestAdapterTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserMapper userMapper;

    @MockitoBean
    private RegisterUserUseCase registerUserUseCase;

    @MockitoBean
    private CheckCredentialUseCase checkCredentialUseCase;

    @Test
    @DisplayName("When User Register Is Correct Expect Return201")
    void When_UserRegisterIsCorrect_Expect_Return201() throws Exception {
        UserRegisterResponse userRegisterResponseMock = TestUtilUser.mockUserResponse();
        User user=TestUtilUser.mockUser();
        Mockito.when(userMapper.userResponseToUser(any())).thenReturn(userRegisterResponseMock);
        Mockito.when(userMapper.userRequestToUser(any())).thenReturn(user);
        Mockito.when(registerUserUseCase.registerUser(any())).thenReturn(user);
        mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestUtilUser.mockUserRequest())))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/v1/users/register/" + userRegisterResponseMock.id().toString()))
                .andExpect(jsonPath("$.id").value(userRegisterResponseMock.id().toString()));
        Mockito.verify(userMapper).userResponseToUser(any());
        Mockito.verify(userMapper).userRequestToUser(any());
        Mockito.verify(registerUserUseCase).registerUser(any());
    }

    @Test
    @DisplayName("When Check Credential User Expect Information User")
    void When_CheckCredentialUser_Expect_InformationUser() throws Exception{
        UserRoleResponse userRoleResponse=TestUtilUser.mockUserRoleResponse();
        AuthRequest authRequest=TestUtilUser.mockAuthRequest();
        User user=TestUtilUser.mockUser();
        Mockito.when(userMapper.authRequestToUser(any())).thenReturn(user);
        Mockito.when(userMapper.userToUserRoleResponse(any())).thenReturn(userRoleResponse);
        Mockito.when(checkCredentialUseCase.checkCredential(any())).thenReturn(user);
        mockMvc.perform(post("/v1/users/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userRoleResponse.id()))
                .andExpect(jsonPath("$.email").value(userRoleResponse.email()))
                .andExpect(jsonPath("$.fullName").value(userRoleResponse.fullName()));
        Mockito.verify(userMapper,times(1)).authRequestToUser(any());
        Mockito.verify(userMapper,times(1)).userToUserRoleResponse(any());
        Mockito.verify(checkCredentialUseCase,times(1)).checkCredential(any());
    }
}
