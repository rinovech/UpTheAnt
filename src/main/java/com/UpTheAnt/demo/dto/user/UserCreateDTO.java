package com.uptheant.demo.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateDTO {

    @NotNull
    private String name;

    @NotNull
    private String username;

    private String email;

    @NotNull
    private String password;
}