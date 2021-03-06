package com.softuni.cuisineonline.service.models.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserLoginServiceModel {

    private String username;

    private String password;
}
