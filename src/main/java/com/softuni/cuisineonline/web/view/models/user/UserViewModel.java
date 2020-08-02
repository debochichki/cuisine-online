package com.softuni.cuisineonline.web.view.models.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserViewModel {

    private String username;

    private String email;

    private String telephoneNumber;

    private String rank;

    private int recipesCount;
}
