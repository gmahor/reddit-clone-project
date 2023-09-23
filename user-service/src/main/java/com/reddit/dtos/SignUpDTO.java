package com.reddit.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class SignUpDTO {

    @NotBlank(message = "Username is required.")
    @Size(min = 2, max = 25, message = "Username must be between 5 and 25")
    private String username;

    @NotBlank(message = "Email is required. ")
    @Size(min = 5, max = 64, message = "Email must be between 5 and 64")
    private String email;

    @NotBlank(message = "Password is required. ")
    @Size(min = 5, max = 64, message = "Password must be between 5 and 64")
    private String password;

}
