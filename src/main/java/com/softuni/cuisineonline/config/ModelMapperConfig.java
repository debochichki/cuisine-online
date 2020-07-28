package com.softuni.cuisineonline.config;

import com.softuni.cuisineonline.data.models.Recipe;
import com.softuni.cuisineonline.service.models.recipe.RecipeUploadServiceModel;
import com.softuni.cuisineonline.web.view.models.recipe.RecipeUploadFormModel;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    private final static Converter<String, Recipe.Type> STRING_TO_RECIPE_TYPE_CONVERTER =
            ctx -> Recipe.Type.valueOf(ctx.getSource().toUpperCase());

    private static final ModelMapper mapper;

    static {
        mapper = new ModelMapper();
        initMapper();
    }

    private static void initMapper() {
        /**
         * RecipeUploadFormModel to RecipeUploadServiceModel mapping configuration
         */
        mapper.createTypeMap(RecipeUploadFormModel.class, RecipeUploadServiceModel.class)
                .addMappings(map -> map
                        .using(STRING_TO_RECIPE_TYPE_CONVERTER)
                        .map(
                                RecipeUploadFormModel::getType,
                                RecipeUploadServiceModel::setType
                        )
                );
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
