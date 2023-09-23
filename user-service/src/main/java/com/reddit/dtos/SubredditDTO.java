package com.reddit.dtos;

import lombok.Data;

@Data
public class SubredditDTO {

    private Long id;

    private String name;

    private String description;

    private Integer numberOfPosts;

}
