package com.fernando.fastticket_user_service.infrastructure.adapters.input;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.fastticket_user_service.application.ports.input.RegisterUserUseCase;
import com.fernando.fastticket_user_service.domain.models.User;
import com.fernando.fastticket_user_service.infrastructure.adapters.input.mappers.UserMapper;
import com.fernando.fastticket_user_service.infrastructure.adapters.input.models.reponses.UserRegisterResponse;
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

    @Test
    @DisplayName("When User Register Is Correct Expect Return201")
    void When_UserRegisterIsCorrect_Expect_Return201() throws Exception {
        UserRegisterResponse userRegisterResponseMock = TestUtilUser.mockUserResponse();
        User user=TestUtilUser.mockUser();
        Mockito.when(userMapper.userResponseToUser(Mockito.any())).thenReturn(userRegisterResponseMock);
        Mockito.when(userMapper.userRequestToUser(Mockito.any())).thenReturn(user);
        Mockito.when(registerUserUseCase.registerUser(Mockito.any())).thenReturn(user);
        mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestUtilUser.mockUserRequest())))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/v1/users/register/" + userRegisterResponseMock.id().toString()))
                .andExpect(jsonPath("$.id").value(userRegisterResponseMock.id().toString()));
        Mockito.verify(userMapper).userResponseToUser(Mockito.any());
        Mockito.verify(userMapper).userRequestToUser(Mockito.any());
        Mockito.verify(registerUserUseCase).registerUser(Mockito.any());
    }
}
