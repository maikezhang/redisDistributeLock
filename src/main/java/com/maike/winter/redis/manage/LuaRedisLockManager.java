package com.maike.winter.redis.manage;

import com.maike.winter.redis.config.LockProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author maike
 * @version 1.0.0
 * @date 2020-01-06 09:49
 */
@Component
public class LuaRedisLockManager implements ILockManager {

    @Autowired
    private ILock distributeLock;

    @Autowired
    private LockProperties lockProperties;

    @Override
    public void callBack(String lockKey, LockCallBack lockCallBack) {

        try {
            String key = this.distributeLock.lock(lockKey, this.lockProperties);
            lockCallBack.execute();
            System.out.println("加锁业务执行成功，lockKey:"+key+"，准备释放锁");
        } finally {
            this.distributeLock.unLock(lockKey, this.lockProperties);
        }


    }

    @Override
    public <T> T callBack(String lockKey, LockRedisReturnCallBack<T> returnCallBack) {

        T result;
        try {
            String key = this.distributeLock.lock(lockKey, this.lockProperties);
            result=returnCallBack.execute();
            System.out.println("加锁业务执行成功，lockKey:"+key+"，准备释放锁");
        } finally {
            this.distributeLock.unLock(lockKey, this.lockProperties);
        }


        return result;
    }
}
