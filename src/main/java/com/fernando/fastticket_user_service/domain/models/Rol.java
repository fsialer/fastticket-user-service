package com.fernando.fastticket_user_service.domain.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Rol {
    private Integer id;
    private String code;
    private String description;
}
