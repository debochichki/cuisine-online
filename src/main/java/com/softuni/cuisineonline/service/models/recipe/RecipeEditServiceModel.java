package com.softuni.cuisineonline.service.models.recipe;

import com.softuni.cuisineonline.data.models.Recipe;
import com.softuni.cuisineonline.service.models.base.BaseServiceModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RecipeEditServiceModel extends BaseServiceModel {

    private String title;

    private MultipartFile image;

    private Recipe.Type type;

    private Integer duration;

    private Byte portions;

    private String ingredientsData;

    private List<String> applianceIds;

    private String description;

}
