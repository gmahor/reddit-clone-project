package com.reddit.dtos;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;
import java.io.Serializable;

@Data
@RedisHash("Users")
public class RedisUserDTO implements Serializable {

    @Id
    private Long id;

    @Indexed
    private String username;

    private String password;

    @Indexed
    private String email;

    private boolean enabled;

    private String roleName;
}
