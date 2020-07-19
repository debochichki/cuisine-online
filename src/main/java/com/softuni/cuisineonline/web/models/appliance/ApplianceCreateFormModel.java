package com.softuni.cuisineonline.web.models.appliance;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class ApplianceCreateFormModel {

    private String name;

    private MultipartFile image;
}
