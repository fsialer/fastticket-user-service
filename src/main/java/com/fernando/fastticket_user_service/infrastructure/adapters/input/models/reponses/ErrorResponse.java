package com.fernando.fastticket_user_service.infrastructure.adapters.input.models.reponses;

import com.fernando.fastticket_user_service.domain.enums.ErrorType;
import lombok.*;

import java.util.List;

@Builder
public record ErrorResponse(String code, ErrorType type, String message, List<String> details, String timestamp) {}
