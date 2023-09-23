package com.reddit.dtos;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

@Data
public class CreateCommentDTO {

    @NotBlank(message = "Text is required")
    private String text;

    @Range(min = 0,message = "post id is required")
    private Long postId;

}
