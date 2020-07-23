package com.softuni.cuisineonline.service.models.appliance;

import com.softuni.cuisineonline.service.models.base.BaseServiceModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplianceServiceModel extends BaseServiceModel {

    private String name;

    private String imageUrl;
}
