package com.example.userauthenticationsystem.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class PasswordModel {
    private String  email;
    private String oldPassword;
    @Length(min=8)
    private String newPassword;

}
