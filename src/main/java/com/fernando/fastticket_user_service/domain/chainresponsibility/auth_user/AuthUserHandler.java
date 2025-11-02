package com.fernando.fastticket_user_service.domain.chainresponsibility.auth_user;

public interface AuthUserHandler {
    AuthUserHandler setNext(AuthUserHandler handler);
    void handle(AuthContext context);
}