package com.softuni.cuisineonline.web.view.models.recipe;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RecipeEditFormModel {

    private String id;

    private String title;

    private MultipartFile image;

    private String type;

    private Integer duration;

    private Byte portions;

    private String ingredientsData;

    private List<String> applianceIds;

    private String description;

}
