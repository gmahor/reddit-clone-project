package com.reddit.dtos;

import lombok.Data;

@Data
public class PostDTO {

    private Long id;

    private String postName;

    private String url;

    private String description;

    private String userName;

    private String subredditName;

}
