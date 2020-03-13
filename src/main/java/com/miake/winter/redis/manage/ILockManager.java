package com.miake.winter.redis.manage;


/**
 * @author maike
 * @version 1.0.0
 * @date 2020-01-06 09:50
 */
public interface ILockManager {

    void callBack(String var1, LockCallBack var2);

    <T> T callBack(String var1, LockRedisReturnCallBack<T> var2);
}
