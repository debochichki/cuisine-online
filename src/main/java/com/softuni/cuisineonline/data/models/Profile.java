package com.softuni.cuisineonline.data.models;

import com.softuni.cuisineonline.data.models.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Profile extends BaseEntity {

    private String username;

    private String email;

    private String telephoneNumber;

    private Rank rank;

    private List<Recipe> recipes;

    private List<Comment> comments;
}
