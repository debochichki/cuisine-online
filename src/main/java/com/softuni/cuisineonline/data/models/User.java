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
@Table(name = "users")
public class User extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1000L;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(unique = true)
    private String telephoneNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_profile", referencedColumnName = "id", nullable = false, unique = true, updatable = false)
    private Profile profile;
}
