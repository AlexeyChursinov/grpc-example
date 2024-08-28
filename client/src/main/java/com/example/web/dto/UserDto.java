package com.example.web.dto;

import jakarta.validation.constraints.Email;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class UserDto {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private RegionDto region;

}
