package com.reddit.dtos;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

@Data
public class CreatePostDTO {

    @NotBlank(message = "Post name is required")
    private String postName;

    @NotBlank(message = "Url name is required")
    private String url;

    @NotBlank(message = "Description name is required")
    private String description;


    @Range(min = 1,message = "subreddit id  is required")
    private Long subredditId;

}
