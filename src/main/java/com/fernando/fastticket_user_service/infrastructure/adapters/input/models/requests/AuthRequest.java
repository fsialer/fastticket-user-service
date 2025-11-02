package com.fernando.fastticket_user_service.infrastructure.adapters.input.models.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    @NotBlank(message = "Field email cannot be blank")
    @Email(message = "Field email must be a valid email")
    private String email;

    @NotBlank(message = "Field password cannot be blank")
    @Length(message = "Field password must be more than 5",min = 5)
    private String password;
}
