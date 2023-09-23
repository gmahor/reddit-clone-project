package com.reddit.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SignInDTO {

    @NotBlank(message = "Username is required.")
    private String username;

    @NotNull(message = "Password is required.")
    private String password;

}
