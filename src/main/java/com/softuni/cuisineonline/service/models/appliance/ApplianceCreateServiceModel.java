package com.softuni.cuisineonline.service.models.appliance;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class ApplianceCreateServiceModel {

    private String name;

    private MultipartFile image;
}
