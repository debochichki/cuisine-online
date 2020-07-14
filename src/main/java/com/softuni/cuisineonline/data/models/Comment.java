package com.softuni.cuisineonline.data.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class Comment {

    private Profile profile;

    private String content;

    private Instant instant;
}
