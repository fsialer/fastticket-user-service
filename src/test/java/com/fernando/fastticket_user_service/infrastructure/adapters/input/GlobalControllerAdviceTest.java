package com.fernando.fastticket_user_service.infrastructure.adapters.input;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.fastticket_user_service.application.ports.input.CheckCredentialUseCase;
import com.fernando.fastticket_user_service.application.ports.input.RegisterUserUseCase;
import com.fernando.fastticket_user_service.domain.exceptions.EmailNotConfirmedException;
import com.fernando.fastticket_user_service.domain.exceptions.PasswordInvalidedException;
import com.fernando.fastticket_user_service.domain.exceptions.UserEmailExistsException;
import com.fernando.fastticket_user_service.domain.exceptions.UserNotFoundException;
import com.fernando.fastticket_user_service.domain.models.User;
import com.fernando.fastticket_user_service.infrastructure.adapters.input.mappers.UserMapper;
import com.fernando.fastticket_user_service.infrastructure.config.TestSecurityConfig;
import com.fernando.fastticket_user_service.utils.TestUtilUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.Mockito;

import static com.fernando.fastticket_user_service.infrastructure.utils.ErrorCatalog.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@WebMvcTest(controllers = {UserRestAdapter.class,GlobalControllerAdvice.class})
class GlobalControllerAdviceTest {
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
    @DisplayName("Expect UserEmailExistsException When EmailUser Exists")
    void Expect_UserEmailExistsException_When_EmailUserExists() throws Exception {
        Mockito.when(registerUserUseCase.registerUser(Mockito.any())).thenThrow(new UserEmailExistsException("Email already exists: john@example.com" ));

        String requestJson = "{\"name\":\"John\",\"email\":\"john@example.com\",\"password\":\"pass123\",\"sex\":\"M\",\"lastName\":\"Doe\"}";

        mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Expect MethodArgumentNotValidException When An Attribute Empty")
    void Expect_MethodArgumentNotValidException_When_AnAttributeEmpty()  throws Exception {
        String requestJson = "{\"name\":\"John\",\"email\":\"john@example.com\",\"password\":\"pass123\",\"sex\":\"M\"}";

        mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
         .andExpect(jsonPath("$.code").value(USER_BAD_PARAMETER.getCode()))
                .andExpect(jsonPath("$.details[0]").value(org.hamcrest.Matchers.containsString("lastName: Field lastName cannot be blank")));
    }

    @Test
    @DisplayName("Expect MethodArgumentNotValidException When Sex Do Not Exists")
    void Expect_MethodArgumentNotValidException_When_SexDoNotExists()  throws Exception {
        String requestJson = "{\"name\":\"John\",\"email\":\"john@example.com\",\"password\":\"pass123\",\"sex\":\"P\",\"lastName\":\"Doe\"}";

        mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(USER_BAD_PARAMETER.getCode()))
                .andExpect(jsonPath("$.details[0]").value(org.hamcrest.Matchers.containsString("sex: Type sex is not valid")));
    }

    @Test
    @DisplayName("Expect EmailNotConfirmedException When Sex Do Not Exists")
    void Expect_EmailNotConfirmedException_When_EmailDoNotConfirmed()  throws Exception {
        String requestJson = "{\"email\":\"john@example.com\",\"password\":\"pass123\"}";
        User user=TestUtilUser.mockUser();
        Mockito.when(userMapper.authRequestToUser(any())).thenReturn(user);
        Mockito.when(checkCredentialUseCase.checkCredential(any())).thenThrow(new EmailNotConfirmedException("Email john@example.com couldn´t be confirm."));

        mockMvc.perform(post("/v1/users/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(EMAIL_NOT_CONFIRMED.getCode()))
                .andExpect(jsonPath("$.message").value(EMAIL_NOT_CONFIRMED.getMessage()))
                .andExpect(jsonPath("$.details[0]").value(org.hamcrest.Matchers.containsString("Email john@example.com couldn´t be confirm.")));
    }

    @Test
    @DisplayName("Expect UserNotFoundException When Email Do Not Exists")
    void Expect_UserNotFoundException_When_EmailDoNotExists()  throws Exception {
        String requestJson = "{\"email\":\"john@example.com\",\"password\":\"pass123\"}";
        User user=TestUtilUser.mockUser();
        Mockito.when(userMapper.authRequestToUser(any())).thenReturn(user);
        Mockito.when(checkCredentialUseCase.checkCredential(any())).thenThrow(new UserNotFoundException("User Not Found: john@example.com"));

        mockMvc.perform(post("/v1/users/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(USER_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(USER_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$.details[0]").value(org.hamcrest.Matchers.containsString("User Not Found: john@example.com")));
    }

    @Test
    @DisplayName("Expect PasswordInvalidedException When Email Do Not Exists")
    void Expect_PasswordInvalidedException_When_EmailDoNotExists()  throws Exception {
        String requestJson = "{\"email\":\"john@example.com\",\"password\":\"pass123\"}";
        User user=TestUtilUser.mockUser();
        Mockito.when(userMapper.authRequestToUser(any())).thenReturn(user);
        Mockito.when(checkCredentialUseCase.checkCredential(any())).thenThrow(new PasswordInvalidedException("Password don´t match."));

        mockMvc.perform(post("/v1/users/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(PASSWORD_INVALIDED.getCode()))
                .andExpect(jsonPath("$.message").value(PASSWORD_INVALIDED.getMessage()))
                .andExpect(jsonPath("$.details[0]").value(org.hamcrest.Matchers.containsString("Password don´t match.")));
    }

    @Test
    @DisplayName("Expect RuntimeException When_OccurredError")
    void Expect_RuntimeException_When_OccurredError() throws Exception {
        Mockito.when(registerUserUseCase.registerUser(Mockito.any())).thenThrow(new RuntimeException("failure"));

        String requestJson = "{\"name\":\"John\",\"email\":\"john@example.com\",\"password\":\"pass123\",\"sex\":\"M\",\"lastName\":\"Doe\"}";

        mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isInternalServerError());
    }
}
