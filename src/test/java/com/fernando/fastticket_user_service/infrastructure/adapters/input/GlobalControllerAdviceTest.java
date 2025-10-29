package com.fernando.fastticket_user_service.infrastructure.adapters.input;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.fastticket_user_service.application.ports.input.RegisterUserUseCase;
import com.fernando.fastticket_user_service.domain.exceptions.UserEmailExistsException;
import com.fernando.fastticket_user_service.infrastructure.adapters.input.mappers.UserMapper;
import com.fernando.fastticket_user_service.infrastructure.config.TestSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.Mockito;


import static com.fernando.fastticket_user_service.infrastructure.utils.ErrorCatalog.USER_BAD_PARAMETER;
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
