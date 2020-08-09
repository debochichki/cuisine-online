package com.softuni.cuisineonline.service.models.appliance;

import com.softuni.cuisineonline.service.models.base.BaseServiceModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplianceServiceModel extends BaseServiceModel {

    private String name;

    private String imageUrl;
}
