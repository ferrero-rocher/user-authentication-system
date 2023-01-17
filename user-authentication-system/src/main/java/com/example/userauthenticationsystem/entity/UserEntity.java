package com.example.userauthenticationsystem.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "username")
    private String username;

    @Column(name = "email",unique = true)
    private String email;

    @Column(length = 60,name="password")
    private String password;
    @Column(name = "role")
    private String role;
    @Column(name = "is_enabled")
    private boolean isEnabled = false;

  /*  public UserEntity(String firstname,String lastname,String email,String password)
    {
        this.firstname=firstname;
        this.lastname=lastname;
        this.email=email;
        this.password=password;
    }*/

}
