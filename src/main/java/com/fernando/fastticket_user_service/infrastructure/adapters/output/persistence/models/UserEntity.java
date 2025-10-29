package com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    @ManyToMany()
    @JoinTable(
            name = "user_roles", // nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "user_id"), // FK hacia User
            inverseJoinColumns = @JoinColumn(name = "role_id") // FK hacia Role
    )
    private Set<RolEntity> roles= new HashSet<>();
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private PersonEntity person;
    @Builder.Default
    private Boolean confirmEmail=Boolean.FALSE;

}
