package com.reddit.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateSubredditDTO {

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "description is required")
    private String description;



}
