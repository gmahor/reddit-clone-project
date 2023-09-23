package com.reddit.dao;

import com.reddit.dtos.RedisPostDTO;
import com.reddit.dtos.RedisUserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Slf4j
@Repository
public class PostDao {

    public static final String HASH_KEY = "Post";

    @Resource(name = "objectRedisTemplate")
    private RedisTemplate<String,Object> template;

    public RedisPostDTO savePostOnRedis(RedisPostDTO redisPostDTO) {
        template.opsForHash().put(HASH_KEY,
                redisPostDTO.getId(),
                redisPostDTO);
        return redisPostDTO;
    }

    public RedisPostDTO getPostById(Long id) {
       return  (RedisPostDTO) template.opsForHash().get(HASH_KEY, id);
    }

}
