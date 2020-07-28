package com.softuni.cuisineonline.web.view.models.recipe;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RecipeUploadFormModel {

    private String title;

    private String type;

    private Integer duration;

    private Byte portions;

    private List<String> applianceIds;

    private MultipartFile image;

    private String description;

    private String ingredientList;
}
