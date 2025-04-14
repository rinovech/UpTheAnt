package com.uptheant.demo.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateDTO {

    @NotNull
    @Size(min = 3, max = 50)
    private String name;

    @NotNull
    @Size(min = 4, max = 50)
    private String username;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 8, max = 50)
    private String password;
}