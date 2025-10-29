package com.fernando.fastticket_user_service.infrastructure.adapters.input.models.reponses;

import lombok.Builder;

@Builder
public record UserRegisterResponse(Long id, String email){}
