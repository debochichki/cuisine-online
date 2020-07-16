package com.softuni.cuisineonline.data.models;

import com.softuni.cuisineonline.data.models.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ingredients")
public class Ingredient extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1000L;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String quantity;

    @Column(name = "unit_of_measurement", nullable = false)
    private String unitOfMeasurement;

    @ManyToOne
    @JoinColumn(name = "fk_recipe", nullable = false, updatable = false)
    private Recipe recipe;
}
