package com.fernando.fastticket_user_service.infrastructure.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCatalog {
    USER_INTERNAL_SERVER_ERROR("USER_000", "Internal server error."),
    USER_BAD_PARAMETER("USER_001", "Invalid parameters for creation customer"),
    USER_EMAIL_EXISTS("USER_002","Email already exists"),
    ROL_NOT_FOUND("USER_003","Rol not found.");
    private final String code;
    private final String message;
}
