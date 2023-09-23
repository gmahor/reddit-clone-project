package com.reddit.dtos;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

@Data
@RedisHash("Post")
public class RedisPostDTO {

    @Id
    private Long id;

    @Indexed
    private String postName;

    private String url;

    private String description;

    @Indexed
    private String userName;

    @Indexed
    private String subredditName;
}
