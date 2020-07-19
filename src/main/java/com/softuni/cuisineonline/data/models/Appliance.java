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
@Table(name = "appliances")
public class Appliance extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1000L;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_image", referencedColumnName = "id", nullable = false)
    private Image image;

    @ManyToMany(mappedBy = "appliances")
    private List<Recipe> recipes;
}
