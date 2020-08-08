package com.softuni.cuisineonline.web.view.models.user;

import com.softuni.cuisineonline.service.models.user.RoleStanding;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRoleViewModel {

    private String id;

    private String username;

    private String email;

    private RoleStanding roleStanding;

}
