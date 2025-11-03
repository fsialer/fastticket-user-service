package com.fernando.fastticket_user_service.domain.chainresponsibility;

public interface AuthUserHandler {
    AuthUserHandler setNext(AuthUserHandler handler);
    void handle(AuthContext context);
}