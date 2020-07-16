package com.softuni.cuisineonline.data.models;

import com.softuni.cuisineonline.data.models.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1000L;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false, updatable = false)
    private Instant instant;

    @ManyToOne
    @JoinColumn(name = "fk_profile", nullable = false, updatable = false)
    private Profile uploader;
}
