package com.reddit.dtos;

import lombok.Data;

import java.time.Instant;

@Data
public class CommentDTO {
    private Long id;
    private Long postId;
    private Instant createdDate;
    private String text;
    private String userName;
}
