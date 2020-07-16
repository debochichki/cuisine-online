package com.softuni.cuisineonline.data.models;

import com.softuni.cuisineonline.data.models.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "recipes")
public class Recipe extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1000L;

    public enum Type {
        SALAD, SOUP, MAIN_DISH, DESSERT, DRINK,
    }

    @Column(nullable = false, unique = true)
    private String title;

    @Transient
    private Image image;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    /**
     * The duration of the recipe preparation
     */
    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false)
    private Byte portions;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<Ingredient> ingredients;

    @ManyToMany
    @JoinTable(
            name = "recipes_appliances",
            joinColumns = @JoinColumn(name = "fk_recipe"),
            inverseJoinColumns = @JoinColumn(name = "fk_appliance")
    )
    private List<Appliance> appliances;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "fk_profile", nullable = false, updatable = false)
    private Profile uploader;
}
