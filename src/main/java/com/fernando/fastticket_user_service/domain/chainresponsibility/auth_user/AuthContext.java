package com.fernando.fastticket_user_service.domain.chainresponsibility.auth_user;

import com.fernando.fastticket_user_service.domain.models.User;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthContext {
    private String email;
    private String password;
    private User storedUser;
}