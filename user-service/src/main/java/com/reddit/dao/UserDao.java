package com.reddit.dao;

import com.reddit.dtos.RedisUserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Slf4j
@Repository
public class UserDao {

    public static final String HASH_KEY = "Users";

    @Resource(name = "objectRedisTemplate")
    private RedisTemplate<String,Object> template;

    public RedisUserDTO saveUserOnRedis(RedisUserDTO redisUserDTO) {
        template.opsForHash().put(HASH_KEY,
                redisUserDTO.getUsername(),
                redisUserDTO);
        return redisUserDTO;
    }

    public RedisUserDTO getUserByUsername(String username) {
       return  (RedisUserDTO) template.opsForHash().get(HASH_KEY, username);
    }

    public RedisUserDTO getUserByUserId(Long id) {
        return  (RedisUserDTO) template.opsForHash().get(HASH_KEY, id);
    }

}
