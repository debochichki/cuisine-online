package com.softuni.cuisineonline.data.models;

import com.softuni.cuisineonline.data.models.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Profile entity contains only domain related data about the User.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "profiles")
public class Profile extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1000L;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rank rank;

    @OneToMany(mappedBy = "uploader", cascade = CascadeType.ALL)
    private List<Recipe> recipes;

    @OneToMany(mappedBy = "uploader", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "uploader", cascade = CascadeType.ALL)
    private List<Video> videos;

    @OneToOne(mappedBy = "profile")
    private User user;
}
