package com.maike.winter.redis.manage;

/**
 * @author maike
 * @version 1.0.0
 * @date 2020-01-06 09:51
 */
public interface LockRedisReturnCallBack<T> {


    T execute();


}
