package com.example.userauthenticationsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {
    @NotBlank(message = "firstname field should be provided ")
    private String firstname;
    @NotBlank(message = "lastname field should be provided ")
    private String lastname;
    @NotBlank(message = "username field should be provided ")
    private String username;
    @NotBlank(message = "email field should be provided ")
    private String email;
    @NotBlank(message = "password field should be provided ")
    @Length(min=8)
    private String password;

}
