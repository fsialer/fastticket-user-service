package com.fernando.fastticket_user_service.domain.models;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends Person{
    private Long id;
    private String email;
    private String password;
    private Set<Rol> roles;

    public User(String email, String password, Set<Rol> roles, String name, String lastName, String sex){
        super(0L,name,lastName,sex);
        this.email=email;
        this.password=password;
        this.roles=roles;
    }

    @Override
    public String fullName() {
        return this.getName()+" "+this.getLastName();
    }
}
