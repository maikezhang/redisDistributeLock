package com.maike.winter.redis.manage;

import com.maike.winter.redis.config.LockProperties;

/**
 * @author maike
 * @version 1.0.0
 * @date 2020-01-06 09:25
 */
public interface ILock {

    String lock(String lockKey, LockProperties properties);

    String unLock(String lockKey, LockProperties properties);

}
