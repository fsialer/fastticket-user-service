package com.fernando.fastticket_user_service.infrastructure.adapters.input.models.reponses;

import lombok.Builder;

import java.util.List;

@Builder
public record UserRoleResponse(Long id, String email, String fullName, List<String> roles) {
}
