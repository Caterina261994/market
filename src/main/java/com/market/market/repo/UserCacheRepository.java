package com.market.market.repo;

import com.market.market.entity.UserCacheEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCacheRepository extends CrudRepository<UserCacheEntity, String> {}
