package com.softuni.cuisineonline.data.models.base;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
//@MappedSuperclass
public abstract class BaseEntity {

    //    @Id
//    @GeneratedValue(generator = "uuid-string")
//    @GenericGenerator(name = "uuid-string", strategy = "org.hibernate.id.UUIDGenerator")
//    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private String id;
}



