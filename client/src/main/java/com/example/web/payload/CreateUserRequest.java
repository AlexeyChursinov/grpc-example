package com.example.web.payload;

import jakarta.validation.constraints.Email;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class CreateUserRequest {

    private String firstName;
    private String lastName;

    @Email(message = "Invalid email format")
    private String email;

    private Integer regionCode;

}
