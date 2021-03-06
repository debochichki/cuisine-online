package com.softuni.cuisineonline.service.models.user;

import com.softuni.cuisineonline.service.models.base.BaseServiceModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserServiceModel extends BaseServiceModel {

    private String username;

    private String email;

    private RoleStanding roleStanding;
}
