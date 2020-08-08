package com.softuni.cuisineonline.service.models.user;

import com.softuni.cuisineonline.service.models.base.BaseServiceModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserProfileServiceModel extends BaseServiceModel {

    private String username;

    private String email;

    private String telephoneNumber;

    private String rank;

    private int recipesCount;
}
