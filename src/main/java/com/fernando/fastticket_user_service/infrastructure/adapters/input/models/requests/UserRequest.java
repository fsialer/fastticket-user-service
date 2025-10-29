package com.fernando.fastticket_user_service.infrastructure.adapters.input.models.requests;

import com.fernando.fastticket_user_service.domain.enums.SexType;
import com.fernando.fastticket_user_service.infrastructure.adapters.input.models.requests.validation.EnumValidator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NotBlank(message = "Field email cannot be blank")
    @Email(message = "Field email must be a valid email")
    private String email;

    @NotBlank(message = "Field password cannot be blank")
    @Length(message = "Field password must be more than 5",min = 5)
    private String password;

    @NotBlank(message = "Field name cannot be blank")
    @Length(message = "Field name must be more than 3",min = 3)
    private String name;

    @NotBlank(message = "Field lastName cannot be blank")
    @Length(message = "Field lastName must be more than 3",min = 3)
    private String lastName;

    @NotBlank(message = "Field sex cannot be blank")
    @EnumValidator(enumClass = SexType.class, message = "Type sex is not valid")
    private String sex;
}
