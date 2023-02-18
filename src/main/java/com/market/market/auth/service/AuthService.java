package com.market.market.auth.service;

import com.market.market.auth.service.dto.UserDto;

public interface AuthService {
    void createCacheUser(UserDto userDto);
}
