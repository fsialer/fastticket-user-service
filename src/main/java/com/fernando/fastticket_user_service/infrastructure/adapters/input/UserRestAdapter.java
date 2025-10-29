package com.fernando.fastticket_user_service.infrastructure.adapters.input;

import com.fernando.fastticket_user_service.application.ports.input.RegisterUserUseCase;
import com.fernando.fastticket_user_service.infrastructure.adapters.input.mappers.UserMapper;
import com.fernando.fastticket_user_service.infrastructure.adapters.input.models.reponses.UserRegisterResponse;
import com.fernando.fastticket_user_service.infrastructure.adapters.input.models.requests.UserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserRestAdapter {

    private final RegisterUserUseCase registerUserUseCase;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> registerUser(@Valid @RequestBody UserRequest rq){
        UserRegisterResponse userRegisterResponse = userMapper.userResponseToUser(registerUserUseCase.registerUser(userMapper.userRequestToUser(rq)));
        String location = "/v1/users/register/".concat(userRegisterResponse.id().toString());
        return ResponseEntity.created(URI.create(location)).body(userRegisterResponse);
    }
}
