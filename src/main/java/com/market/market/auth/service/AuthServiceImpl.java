package com.market.market.auth.service;

import com.market.market.auth.service.dto.UserDto;
import com.market.market.entity.UserCacheEntity;
import com.market.market.repo.UserCacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{
    @Autowired
    private UserCacheRepository userCacheRepository;
    @Override
    public void createCacheUser(UserDto userDto) {
        UserCacheEntity userCacheEntity = new UserCacheEntity(userDto);
        userCacheEntity = userCacheRepository.save(userCacheEntity);
    }
}
