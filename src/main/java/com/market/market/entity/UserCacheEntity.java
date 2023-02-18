package com.market.market.entity;

import com.market.market.auth.service.dto.UserDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@RedisHash("UserCacheEntity")
@Setter
@Getter
public class UserCacheEntity {
    @Id
    private String id;
    private UserDto userDTO;

    public UserCacheEntity(UserDto userDTO) {
        this.id = UUID.randomUUID().toString();
        this.userDTO = userDTO;
    }
}

