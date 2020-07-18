package com.softuni.cuisineonline.web.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRegisterFormModel {

    private String username;

    private String password;

    private String confirmPassword;

    private String email;

    private String telephoneNumber;
}
