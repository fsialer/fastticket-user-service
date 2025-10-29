package com.fernando.fastticket_user_service.domain.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class Person {
    private Long id;
    private String name;
    private String lastName;
    private String sex;
    public abstract String fullName();
}
